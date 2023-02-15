**iTFM:
 An improved triangular form-based multiple flow direction algorithm**

--------------------------------------------------------------------------
Last edit: Oct., 31, 2021 

**1. Distribution and Copyright**

Copyright (c) 2021 Pengfei Wu, Jintao Liu, Xiaole Han, Meiyan Feng and Junyuan Fei

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

If you wish to use or incorporate this program (or parts of it) into other software that does not meet the GNU General Public License conditions contact the author to request permission.

**Pengfei Wu**

College of Hydrology and Water Resources, Hohai University

1st Xikang Rd., Nanjing 210098, Jiangsu Province, People's Republic of China

Work phone: +86 18351936707

E-mail: wpf@hhu.edu.cn

**Jintao Liu**

College of Hydrology and Water Resources, Hohai University

1st Xikang Rd., Nanjing 210098, Jiangsu Province, People's Republic of China

Work phone: +86 13327836738

E-mail: jtliu@hhu.edu.cn

--------------------------------------------------------------------------
**2. The Algorithms**

The iTFM algorithms are described in Wu et al. [2022]. The Java codes that implement the iTFM as well as several existing algorithms are provided. The original paper should be cited appropriately whenever iTFM is used.
Citation:
Wu, P., Liu, J., Han, X., Feng, M., Fei, J., & Shen, X. (2022). An improved triangular form-based multiple flow direction algorithm for determining the nonuniform flow domain over grid networks. Water Resources Research, 58, e2021WR031706. https://doi.org/10.1029/2021WR031706

--------------------------------------------------------------------------
**3. File Description**

Folder 'codes': Java codes for the algorithms, and 'MainClass.java' contains the main class.

iTFM.zip: An entire project of the algorithms for the Eclipse compilation tool with Windows system.

Himmelblau.txt: an ASCII DEM file for example.

--------------------------------------------------------------------------
**4. Things to Keep in Mind**

1. The import DEM should be in the ASCII TXT format, and please keep the six rows of headers.

2. There is a function to remove flats and depressions in the codes, but it is disabled.
