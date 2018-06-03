# K-means MapReduce implementation
In this work clustering algorithm *k-means* is implemented using MapReduce (Hadoop ver. 2.8) fremework. 

To run the program, shell script ```run.sh``` should be executed. It requires path to jar file and its input parameters which are:

* ```input``` - path to data file
* ```state``` - path to file that contains clusters 
* ```number``` - number of reducers 
* ```output``` - output directory 
* ```delta``` - convergence threshold (acceptable difference between 2 subsequent centroids)
* ```max``` - maximum number of iterations 
* ```distance``` - similairty measure (currently only Euclidean distance is supported)

## Workflow
The figure below denotes one iteration of MapReduce program.

![alt text][flow]

First, Centroids and Context (Configuration) are loaded into the Distributed Cache. This is done by overriding setup function in the Mapper and Reducer class. Afterwards, the input data file is split and each data point is processed by one of the map functions (in Map process). The function writes key-value pairs <Centroid, Point>, where the Centroid is the closest one to the Point. Next, Combiner is used in order to decrease the number of local writings. In this phase data points that are on the same machine are summed up and the number of those data points is recorded, Point.number variable. Now, for the optimization reasons output values are automatically shuffled and sorted by Centroids. The Reducer performs the same procedure as the Combiner, but it also checks whether centroids converged. If they did, then global Counter is incremented. 
After the one iteration is done, the program checks two conditions, if the program reached the maximum number of iterations, and if the Counter value is unchanged. If one of these conditions is satisfied, then the program is finished, otherwise, the whole MapReduce process is run again with the updated centroids.

## Examples
One of the use-cases of k-means algorithm is the color quantization process, reducing the number of distinct colors of an image. (Far better algorithms for this purpose are available)

Numerical (RGB) values of images (Fig. 1) are saved as input data (Fig. 2), and clusters are randomly initialized. 


### Original Images

![alt text][fig1]


### RGB values of original and modified images  

![alt text][fig2]

#### After 10 iterations with 10 clusters, RBG values are represented in Fig. 3. It can be noted that a couple of centroids have vanished. 

![alt text][fig3]

### Modified images for different number of centroids 

![alt text][fig4]

### Modified images for different number of iterations and 10 centroids 

![alt text][fig5]

![alt text][fig6]


[flow]: https://github.com/Maki94/kmeans_mapreduce/blob/master/figures/alg.png "One MapReduce iteration"

[fig1]: https://github.com/Maki94/kmeans_mapreduce/blob/master/figures/fig1.PNG "Original images"
[fig2]: https://github.com/Maki94/kmeans_mapreduce/blob/master/figures/fig2.PNG "RGB model"
[fig3]: https://github.com/Maki94/kmeans_mapreduce/blob/master/figures/fig3.PNG "10th iteration, 10 clusters"
[fig4]: https://github.com/Maki94/kmeans_mapreduce/blob/master/figures/fig4.PNG "Different number of clusters, 10th iteration"
[fig5]: https://github.com/Maki94/kmeans_mapreduce/blob/master/figures/fig5.PNG "Different number of iterations, 10 clusters"
[fig6]: https://github.com/Maki94/kmeans_mapreduce/blob/master/figures/fig6.PNG "Different number of iterations, 10 clusters"
