# Scripts to prepare input data and visualize results for image examples

`data_prep.py` script creates points.txt and clusters.txt files for a given input image.

`visualize_results.py` script writes an image for estimated centroids.

### Requirements
- Python 3 (tested on version 3.8.2)
- numpy (tested on version 1.19.2)
- opencv (tested on version 4.4.0.42)

### Installation

#### Setup virtualenv
```bash
virtualenv venv -p python3
source venv/bin/activate
pip install -r requirements.txt
```

## Program parameters for `data_prep.py` 

- `src_img` - Path to the source image.
- `dst_folder` - Directory in which points.txt and clusters.txt will be saved.
- `k_init_centroids` - How many initial uniformly sampled centroids to generate.

## Program parameters for `visualize_results.py`

- `clusters_path` - File or directory path to generated clusters.
- `src_img` - Path to the source image.
- `dst_img` - Path to the image to be written.

## Sample usage:

The example below creates `points.txt` file for a given image (`src_img`) and generates uniformly sampled initial centroids. 

```bash
source venv/bin/activate

python data_prep.py --src_img ./sample_images/image2.jpg --dst_folder ./input_data --k_init_centroids 10
```

The example below visualizes estimated clusters. `points.txt` file for a given image (`src_img`) and generates uniformly sampled initial centroids.


```bash
source venv/bin/activate

python visualize_results.py --clusters_path ./input_data/clusters.txt --src_img ./sample_images/image2.jpg --dst_img ./tmp.jpg
```