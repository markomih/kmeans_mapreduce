import argparse
from os.path import join
from pathlib import Path

import cv2
import numpy as np

parser = argparse.ArgumentParser(description='This script creates points.txt and clusters.txt files for a given image.')

parser.add_argument('--src_img', type=str, help='Path to the source image.')
parser.add_argument('--dst_folder', type=str, help='Directory in which points.txt and clusters.txt will be saved.')
parser.add_argument('--k_init_centroids', type=int, help='How many initial uniformly sampled centroids to generate.',
                    default=10)

args = parser.parse_args()


def nparray_to_str(X):
    to_save = '\n'.join([' '.join(str(X[i])[1:-1].split()) for i in range(len(X))])
    return to_save


def main(src_img, dst_folder, k):
    # files to be created
    points_path = join(dst_folder, 'points.txt')
    clusters_path = join(dst_folder, 'clusters.txt')

    # create directory
    Path(dst_folder).mkdir(parents=True, exist_ok=True)

    # load and write points
    img = cv2.imread(src_img).reshape((-1, 3)).astype(np.float32)
    with open(points_path, 'w') as f:
        f.write(nparray_to_str(img))
    print(f'Points saved in: {points_path}')

    # generate and save uniformly sampled centroids
    s = np.random.uniform(low=img.min(), high=img.max(), size=(k, 3))
    tmp_labels = np.arange(1, k + 1).reshape((k, 1))
    clusters = np.hstack((tmp_labels, s))

    with open(clusters_path, 'w') as f:
        f.write(nparray_to_str(clusters))
    print(f'Centroids saved in: {clusters_path}')


if __name__ == '__main__':
    args = parser.parse_args()
    main(args.src_img, args.dst_folder, args.k_init_centroids)
