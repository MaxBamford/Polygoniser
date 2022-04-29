# Polygoniser

In its current form the project is capable of splitting an n-sided shape into component n-sided polygons. This process is semi-random but certain bias's have been developed to improve the end result. Currently, these are as follows:

- FavourLength: This variable appears as a checkbox in the main UI and causes the longest side of a given shape to be chosen when a shape is being split, the other side is random. This reduces the likelihood of forming very long polygons
- AreaBias: This splitting method performs the split on a list ordered by area so the largest shapes are the most likely to be split
- LengthBias: This splitting method favors shapes with the longest single side length
- VarianceBias: This splitting method splits the shapes with the largest local variance - in an attempt to have more detailed sections of the image use more polygons while big single colour areas should use fewer (currently slow & WIP)