import argparse
import glob
import io
import os
import random
import numpy
import cv2
from PIL import Image, ImageFont, ImageDraw
from scipy.ndimage.interpolation import map_coordinates
from scipy.ndimage.filters import gaussian_filter
from matplotlib import pyplot as plt

SCRIPT_PATH = os.path.dirname(os.path.abspath(__file__))

# DEFAULT_LABEL_FILE = os.path.join(SCRIPT_PATH,
#                                   '../labels/hangul-label.txt')
                            
DEFAULT_LABEL_FILE = os.path.join(SCRIPT_PATH,
                                  '../labels/1-common-hangul.txt')

DEFAULT_FONTS_DIR = os.path.join(SCRIPT_PATH, '../fonts')
DEFAULT_OUTPUT_DIR = os.path.join(SCRIPT_PATH, '../image-data')

DISTORTION_COUNT = 3

IMAGE_WIDTH = 128
IMAGE_HEIGHT = 128

array_fontsize = [45, 65]
def generate_hangul_images(label_file, fonts_dir, output_dir):
    with io.open(label_file, 'r', encoding='utf-8') as f:
        labels = f.read().splitlines()

    image_dir = os.path.join(output_dir, 'hangul-images')
    if not os.path.exists(image_dir):
        os.makedirs(os.path.join(image_dir))

    fonts = glob.glob(os.path.join(fonts_dir, '*.ttf'))

    labels_csv = io.open(os.path.join(output_dir, 'labels-map.csv'), 'w',
                         encoding='utf-8')

    total_count = 0
    prev_count = 0
    for character in labels:

        for size in array_fontsize:
            
            if total_count - prev_count > 5000:
                prev_count = total_count
                print('{} images generated...'.format(total_count))

            for font in fonts:
                total_count += 1
                image = Image.new('L', (IMAGE_WIDTH, IMAGE_HEIGHT), color=255)
                font = ImageFont.truetype(font, size)
                drawing = ImageDraw.Draw(image)
                w, h = drawing.textsize(character, font=font)
                drawing.text(
                    ((IMAGE_WIDTH-w)/2, (IMAGE_HEIGHT-h)/2),
                    character,
                    fill=(0),
                    font=font
                )
                file_string = 'hangul_{}.jpeg'.format(total_count)
                file_path = os.path.join(image_dir, file_string)
                image_crop = crop_image(image)
                image_crop.save(file_path, 'JPEG')
                labels_csv.write(u'{},{}\n'.format(file_path, character))

                # affine_right
                total_count += 1
                file_string = 'hangul_{}.jpeg'.format(total_count)
                file_path = os.path.join(image_dir, file_string)
                affine_image = numpy.array(image)
                affine_right_image = affine_right(affine_image)
                cv2.imwrite(file_path, affine_right_image)
                labels_csv.write(u'{},{}\n'.format(file_path, character))

                # affine_left
                total_count += 1
                file_string = 'hangul_{}.jpeg'.format(total_count)
                file_path = os.path.join(image_dir, file_string)
                affine_image = numpy.array(image)
                affine_left_image = affine_left(affine_image)
                cv2.imwrite(file_path, affine_left_image)
                labels_csv.write(u'{},{}\n'.format(file_path, character))

                for i in range(DISTORTION_COUNT):
                    total_count += 1
                    file_string = 'hangul_{}.jpeg'.format(total_count)
                    file_path = os.path.join(image_dir, file_string)
                    arr = numpy.array(image)

                    distorted_array = elastic_distort(
                        arr, alpha=random.randint(30, 36),
                        sigma=random.randint(5, 6)
                    )

                    distorted_image = Image.fromarray(distorted_array)
                    distorted_image_crop = crop_image(distorted_image)

                    distorted_image_crop.save(file_path, 'JPEG')
                    labels_csv.write(u'{},{}\n'.format(file_path, character))

                    ## affine_right
                    total_count += 1
                    file_string = 'hangul_{}.jpeg'.format(total_count)
                    file_path = os.path.join(image_dir, file_string)
                    distorted_image = numpy.array(distorted_array)
                    affine_right_image = affine_right(distorted_image)
                    cv2.imwrite(file_path, affine_right_image)
                    labels_csv.write(u'{},{}\n'.format(file_path, character))

                    ## affine_right
                    total_count += 1
                    file_string = 'hangul_{}.jpeg'.format(total_count)
                    file_path = os.path.join(image_dir, file_string)
                    distorted_image = numpy.array(distorted_array)
                    affine_left_image = affine_left(distorted_image)
                    cv2.imwrite(file_path, affine_left_image)
                    labels_csv.write(u'{},{}\n'.format(file_path, character))
                    

    print('Finished generating {} images.'.format(total_count))
    labels_csv.close()


def elastic_distort(image, alpha, sigma):
    random_state = numpy.random.RandomState(None)
    shape = image.shape

    dx = gaussian_filter(
        (random_state.rand(*shape) * 2 - 1),
        sigma, mode="constant"
    ) * alpha
    dy = gaussian_filter(
        (random_state.rand(*shape) * 2 - 1),
        sigma, mode="constant"
    ) * alpha

    x, y = numpy.meshgrid(numpy.arange(shape[0]), numpy.arange(shape[1]))
    indices = numpy.reshape(y+dy, (-1, 1)), numpy.reshape(x+dx, (-1, 1))
    return map_coordinates(image, indices, order=1).reshape(shape)

def affine_right(img):
    rows, cols = img.shape
    pts1 = numpy.float32([[40,20],[80,20],[40,40]])
    pts2 = numpy.float32([[40,40],[80,20],[40,60]]) # 오른쪽 위

    M = cv2.getAffineTransform(pts1, pts2)
    dst = cv2.warpAffine(img, M, (cols,rows))

    dst = dst[50:114, 32:96]
    return dst

def affine_left(img):
    rows, cols = img.shape
    pts1 = numpy.float32([[40,20],[80,20],[40,40]])
    pts2 = numpy.float32([[40,20],[80,40],[40,40]]) # 오른쪽 아래

    M = cv2.getAffineTransform(pts1, pts2)
    dst = cv2.warpAffine(img, M, (cols,rows))

    dst = dst[50:114, 32:96]
    return dst

def crop_image(image):
    area = (32, 40, 96, 104)
    image = image.crop(area)
    return image

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--label-file', type=str, dest='label_file',
                        default=DEFAULT_LABEL_FILE,
                        help='File containing newline delimited labels.')
    parser.add_argument('--font-dir', type=str, dest='fonts_dir',
                        default=DEFAULT_FONTS_DIR,
                        help='Directory of ttf fonts to use.')
    parser.add_argument('--output-dir', type=str, dest='output_dir',
                        default=DEFAULT_OUTPUT_DIR,
                        help='Output directory to store generated images and '
                             'label CSV file.')
    args = parser.parse_args()
    generate_hangul_images(args.label_file, args.fonts_dir, args.output_dir)