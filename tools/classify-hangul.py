import argparse
import io
import os
import sys

import tensorflow as tf
import numpy

SCRIPT_PATH = os.path.dirname(os.path.abspath(__file__))

DEFAULT_LABEL_FILE = os.path.join(
    SCRIPT_PATH, '../labels/2350-common-hangul.txt'
)
DEFAULT_GRAPH_FILE = os.path.join(
    SCRIPT_PATH, '../saved-model/optimized_hangul_tensorflow.pb'
)

def read_image(file):
    file_content = tf.io.read_file(file)
    image = tf.image.decode_jpeg(file_content, channels=1)
    image = tf.image.convert_image_dtype(image, dtype=tf.float32)
    image = tf.reshape(image, [64*64])
    return image


def classify(args):
    args.image = './hangul_sample/hangul_sample1.jpeg'
    
    labels = io.open(args.label_file,
                     'r', encoding='utf-8').read().splitlines()

    if not os.path.isfile(args.image):
        print('Error: Image %s not found.' % args.image)
        sys.exit(1)

    with tf.io.gfile.GFile(args.graph_file, "rb") as f:
        graph_def = tf.compat.v1.GraphDef()
        graph_def.ParseFromString(f.read())

    with tf.Graph().as_default() as graph:
        tf.import_graph_def(
            graph_def,
            input_map=None,
            return_elements=None,
            name='hangul-model',
            producer_op_list=None
        )
    
    x = graph.get_tensor_by_name('hangul-model/input:0')
    y = graph.get_tensor_by_name('hangul-model/output:0')
    keep_prob = graph.get_tensor_by_name('hangul-model/keep_prob:0')

    image = read_image(args.image)
    sess = tf.InteractiveSession()
    image_array = sess.run(image)
    sess.close()

    with tf.compat.v1.Session(graph=graph) as graph_sess:
        predictions = graph_sess.run(y, feed_dict={x: image_array.reshape(1,4096),
                                                   keep_prob: 1.0})
        prediction = predictions[0]

    sorted_indices = prediction.argsort()[::-1][:5]

    for index in sorted_indices:
        label = labels[index]
        confidence = prediction[index]
        print('%s (confidence = %.5f)' % (label, confidence))

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    # parser.add_argument('image', type=str,
    #                     help='Image to pass to model for classification.')
    parser.add_argument('--label-file', type=str, dest='label_file',
                        default=DEFAULT_LABEL_FILE,
                        help='File containing newline delimited labels.')
    parser.add_argument('--graph-file', type=str, dest='graph_file',
                        default=DEFAULT_GRAPH_FILE,
                        help='The saved model graph file to use for '
                             'classification.')
    classify(parser.parse_args())
