import argparse
from glob import glob
from os.path import join, isdir, isfile

import cv2
import numpy as np

parser = argparse.ArgumentParser(description='This script visualizes estimated clusters.')

parser.add_argument('--clusters_path', type=str, help='File or directory path to generated clusters.')
parser.add_argument('--src_img', type=str, help='Path to the source image.')
parser.add_argument('--dst_img', type=str, help='Path to the image to be written.')


args = parser.parse_args()


def load_clusters(path):
    if isdir(path):
        files = glob(join(path, 'part-r-*[0-9]'))
    elif isfile(path):
        files = [path]
    else:
        raise Exception('Invalid file path.')

    centroids = [load_nparray(file)[:, 1:] for file in files]
    centroids = np.concatenate(centroids, axis=0).reshape(-1, centroids[0].shape[-1])
    return centroids


def load_nparray(file):
    data = []
    with open(file) as f:
        for line in f:
            data.append(np.array([float(num) for num in line.split(' ')]))

    return np.stack(data).astype(np.float)


def main(clusters_path, src_img, dst_img):
    clusters = load_clusters(clusters_path)
    img = cv2.imread(src_img)
    shape = img.shape

    img = img.reshape((-1, 3))
    new_image = np.zeros_like(img)
    for i in range(img.shape[0]):
        ind = np.linalg.norm(clusters - img[i], axis=-1).argmin()
        new_image[i] = clusters[ind].astype(np.uint8)

    cv2.imwrite(dst_img, new_image.reshape(shape))


if __name__ == '__main__':
    args = parser.parse_args()
    main(args.clusters_path, args.src_img, args.dst_img)
