import argparse
import io
import os

import tensorflow as tf
from tensorflow.python.tools import freeze_graph
from tensorflow.python.tools import optimize_for_inference_lib


SCRIPT_PATH = os.path.dirname(os.path.abspath(__file__))

DEFAULT_LABEL_FILE = os.path.join(SCRIPT_PATH,
                                  './labels/2350-common-hangul.txt')
DEFAULT_TFRECORDS_DIR = os.path.join(SCRIPT_PATH, 'tfrecords-output')
DEFAULT_OUTPUT_DIR = os.path.join(SCRIPT_PATH, 'saved-model')

MODEL_NAME = 'hangul_tensorflow'
IMAGE_WIDTH = 64
IMAGE_HEIGHT = 64

DEFAULT_NUM_EPOCHS = 15
BATCH_SIZE = 100

num_classes = 2350


def _parse_function(example):
    features = tf.parse_single_example(
        example,
        features={
            'image/class/label': tf.FixedLenFeature([], tf.int64),
            'image/encoded': tf.FixedLenFeature([], dtype=tf.string,
                                                default_value='')
        })
    label = features['image/class/label']
    image_encoded = features['image/encoded']

    image = tf.image.decode_jpeg(image_encoded, channels=1)
    image = tf.image.convert_image_dtype(image, dtype=tf.float32)
    image = tf.reshape(image, [IMAGE_WIDTH*IMAGE_HEIGHT])

    label = tf.stack(tf.one_hot(label, num_classes))
    return image, label


def export_model(model_output_dir, input_node_names, output_node_name):
    name_base = os.path.join(model_output_dir, MODEL_NAME)
    frozen_graph_file = os.path.join(model_output_dir,
                                     'frozen_' + MODEL_NAME + '.pb')
    freeze_graph.freeze_graph(
        name_base + '.pbtxt', None, False, name_base + '.chkp',
        output_node_name, "save/restore_all", "save/Const:0",
        frozen_graph_file, True, ""
    )

    input_graph_def = tf.GraphDef()
    with tf.gfile.Open(frozen_graph_file, "rb") as f:
        input_graph_def.ParseFromString(f.read())

    output_graph_def = optimize_for_inference_lib.optimize_for_inference(
            input_graph_def, input_node_names, [output_node_name],
            tf.float32.as_datatype_enum)

    optimized_graph_file = os.path.join(model_output_dir,
                                        'optimized_' + MODEL_NAME + '.pb')
    with tf.gfile.GFile(optimized_graph_file, "wb") as f:
        f.write(output_graph_def.SerializeToString())

    print("Inference optimized graph saved at: " + optimized_graph_file)


def weight_variable(shape):
    initial = tf.random.truncated_normal(shape, stddev=0.1)
    return tf.Variable(initial, name='weight')


def bias_variable(shape):
    initial = tf.constant(0.1, shape=shape)
    return tf.Variable(initial, name='bias')


def main(label_file, tfrecords_dir, model_output_dir, num_train_epochs):
    labels = io.open(label_file, 'r', encoding='utf-8').read().splitlines()
    num_classes = len(labels)

    input_node_name = 'input'
    keep_prob_node_name = 'keep_prob'
    output_node_name = 'output'

    if not os.path.exists(model_output_dir):
        os.makedirs(model_output_dir)

    print('Processing data...')

    tf_record_pattern = os.path.join(tfrecords_dir, '%s-*' % 'train')
    train_data_files = tf.io.gfile.glob(tf_record_pattern)

    tf_record_pattern = os.path.join(tfrecords_dir, '%s-*' % 'test')
    test_data_files = tf.io.gfile.glob(tf_record_pattern)

    train_dataset = tf.data.TFRecordDataset(train_data_files) \
        .map(_parse_function) \
        .shuffle(1000) \
        .repeat(num_train_epochs) \
        .batch(BATCH_SIZE) \
        .prefetch(1)


    x = tf.compat.v1.placeholder(tf.float32, [None, IMAGE_WIDTH*IMAGE_HEIGHT],
                       name=input_node_name)
    
    y_ = tf.compat.v1.placeholder(tf.float32, [None, num_classes])

    x_image = tf.reshape(x, [-1, IMAGE_WIDTH, IMAGE_HEIGHT, 1])

    W_conv1 = weight_variable([5, 5, 1, 32])
    b_conv1 = bias_variable([32])
    x_conv1 = tf.nn.conv2d(x_image, W_conv1, strides=[1, 1, 1, 1],
                           padding='SAME')
    h_conv1 = tf.nn.relu(x_conv1 + b_conv1)

    h_pool1 = tf.nn.max_pool(h_conv1, ksize=[1, 2, 2, 1],
                             strides=[1, 2, 2, 1], padding='SAME')

    W_conv2 = weight_variable([5, 5, 32, 64])
    b_conv2 = bias_variable([64])
    x_conv2 = tf.nn.conv2d(h_pool1, W_conv2, strides=[1, 1, 1, 1],
                           padding='SAME')
    h_conv2 = tf.nn.relu(x_conv2 + b_conv2)

    h_pool2 = tf.nn.max_pool(h_conv2, ksize=[1, 2, 2, 1],
                             strides=[1, 2, 2, 1], padding='SAME')

    W_conv3 = weight_variable([3, 3, 64, 128])
    b_conv3 = bias_variable([128])
    x_conv3 = tf.nn.conv2d(h_pool2, W_conv3, strides=[1, 1, 1, 1],
                           padding='SAME')
    h_conv3 = tf.nn.relu(x_conv3 + b_conv3)

    h_pool3 = tf.nn.max_pool(h_conv3, ksize=[1, 2, 2, 1],
                             strides=[1, 2, 2, 1], padding='SAME')

    h_pool_flat = tf.reshape(h_pool3, [-1, 8*8*128])
    W_fc1 = weight_variable([8*8*128, 1024])
    b_fc1 = bias_variable([1024])
    h_fc1 = tf.nn.relu(tf.matmul(h_pool_flat, W_fc1) + b_fc1)

    keep_prob = tf.compat.v1.placeholder(tf.float32, name=keep_prob_node_name)
    h_fc1_drop = tf.nn.dropout(h_fc1, rate=1-keep_prob)

    W_fc2 = weight_variable([1024, num_classes])
    b_fc2 = bias_variable([num_classes])
    y = tf.matmul(h_fc1_drop, W_fc2) + b_fc2

    tf.nn.softmax(y, name=output_node_name)

    cross_entropy = tf.reduce_mean(
        tf.nn.softmax_cross_entropy_with_logits_v2(
            labels=tf.stop_gradient(y_),
            logits=y
        )
    )

    train_step = tf.compat.v1.train.AdamOptimizer(0.0001).minimize(cross_entropy)

    correct_prediction = tf.equal(tf.argmax(y, 1), tf.argmax(y_, 1))
    correct_prediction = tf.cast(correct_prediction, tf.float32)
    accuracy = tf.reduce_mean(correct_prediction)

    saver = tf.compat.v1.train.Saver()

    with tf.compat.v1.Session() as sess:
        sess.run(tf.compat.v1.global_variables_initializer())

        checkpoint_file = os.path.join(model_output_dir, MODEL_NAME + '.chkp')

        tf.io.write_graph(sess.graph_def, model_output_dir,
                             MODEL_NAME + '.pbtxt', True)

        try:
            iterator = train_dataset.make_one_shot_iterator()
            batch = iterator.get_next()
            step = 0

            while True:

                train_images, train_labels = sess.run(batch)

                sess.run(train_step, feed_dict={x: train_images,
                                                y_: train_labels,
                                                keep_prob: 0.5})
                if step % 100 == 0:
                    train_accuracy = sess.run(
                        accuracy,
                        feed_dict={x: train_images, y_: train_labels,
                                   keep_prob: 1.0}
                    )
                    print("Step %d, Training Accuracy %g" %
                          (step, float(train_accuracy)))

                if step % 10000 == 0:
                    saver.save(sess, checkpoint_file, global_step=step)

                step += 1

        except tf.errors.OutOfRangeError:
            pass

        saver.save(sess, checkpoint_file)

        print('Testing model...')

        test_dataset = tf.data.TFRecordDataset(test_data_files) \
            .map(_parse_function) \
            .batch(BATCH_SIZE) \
            .prefetch(1)

        accuracy2 = tf.reduce_sum(correct_prediction)
        total_correct_preds = 0
        total_preds = 0

        try:
            iterator = test_dataset.make_one_shot_iterator()
            batch = iterator.get_next()
            while True:
                test_images, test_labels = sess.run(batch)
                acc = sess.run(accuracy2, feed_dict={x: test_images,
                                                     y_: test_labels,
                                                     keep_prob: 1.0})
                total_preds += len(test_images)
                total_correct_preds += acc

        except tf.errors.OutOfRangeError:
            pass

        test_accuracy = total_correct_preds/total_preds
        print("Testing Accuracy {}".format(test_accuracy))

        export_model(model_output_dir, [input_node_name, keep_prob_node_name],
                     output_node_name)

        sess.close()


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--label-file', type=str, dest='label_file',
                        default=DEFAULT_LABEL_FILE,
                        help='File containing newline delimited labels.')
    parser.add_argument('--tfrecords-dir', type=str, dest='tfrecords_dir',
                        default=DEFAULT_TFRECORDS_DIR,
                        help='Directory of TFRecords files.')
    parser.add_argument('--output-dir', type=str, dest='output_dir',
                        default=DEFAULT_OUTPUT_DIR,
                        help='Output directory to store saved model files.')
    parser.add_argument('--num-train-epochs', type=int,
                        dest='num_train_epochs',
                        default=DEFAULT_NUM_EPOCHS,
                        help='Number of times to iterate over all of the '
                             'training data.')
    args = parser.parse_args()
    main(args.label_file, args.tfrecords_dir,
         args.output_dir, args.num_train_epochs)