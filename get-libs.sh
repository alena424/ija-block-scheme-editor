#!/bin/sh

# IJA Project
# get-libs.sh
# author: xtesar36

wget http://search.maven.org/remotecontent?filepath=org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
wget http://www.migcalendar.com/miglayout/versions/4.0/miglayout-4.0-swing.jar

mkdir img
mkdir img/selected

# IMAGES
wget -P $(dirname $0)/img "https://drive.google.com/uc?export=download&id=1BMhli9LGq1sfOSr2PEdDwy9mbzz60ryd" -O random.png
wget -P $(dirname $0)/img "https://drive.google.com/uc?export=download&id=1T_DOF7Bfr-jdDGiWi60Z4nwazBjeug16" -O multiplication.png
wget -P $(dirname $0)/img "https://drive.google.com/uc?export=download&id=12k12sD6zF23e_JZpqlwuEHCWbjZD1qEI" -O minus.png
wget -P $(dirname $0)/img "https://drive.google.com/uc?export=download&id=19j9lGCxAGPpVuEIJEH7hSgnjABF1q_vt" -O invert.png
wget -P $(dirname $0)/img "https://drive.google.com/uc?export=download&id=1fYPVA7kZQMY8qsiUBI1Wmtohb3gDGnSO" -O if_check_13491.png
wget -P $(dirname $0)/img "https://drive.google.com/uc?export=download&id=1emfgSh74GcyXLhzPJBADfl8mw9E_tfHv" -O if_close_13493.png
wget -P $(dirname $0)/img "https://drive.google.com/uc?export=download&id=1EPnTf35Lp1B8IBtxF3HlVNm-STnhKewZ" -O equals.png
wget -P $(dirname $0)/img "https://drive.google.com/uc?export=download&id=17wufVaNqML839pT2sDm3JofGPZWaiEwe" -O division.png
wget -P $(dirname $0)/img "https://drive.google.com/uc?export=download&id=10F31pZHcXvHM1aDWn2OX1xqV2zYQfBAb" -O default.png
wget -P $(dirname $0)/img "https://drive.google.com/uc?export=download&id=1ezchHfB5O9yYVK1a_KdUdRrYzM_EFob4" -O default_check.png
wget -P $(dirname $0)/img "https://drive.google.com/uc?export=download&id=1dC-JAPqxtfZXFdEo_OGHENFZ04MDUnIT" -O addition.png

# SELECTED IMAGES
wget -P $(dirname $0)/img/selected "https://drive.google.com/uc?export=download&id=1NtWKG5YIaldCS5QGc1mhsVb-7Db5y4e9" -O random.png
wget -P $(dirname $0)/img/selected "https://drive.google.com/uc?export=download&id=1-ySZk3Y7b4dzkgKD1nMRTbc2QL_nHUft" -O multiplication.png
wget -P $(dirname $0)/img/selected "https://drive.google.com/uc?export=download&id=1QGx1Ca8PUt6w0N2_tQqeOVKnV65O7nPE" -O minus.png
wget -P $(dirname $0)/img/selected "https://drive.google.com/uc?export=download&id=1UHN8TtyEH-HZz86U5zHKl7iNw5cl4alr" -O invert.png
wget -P $(dirname $0)/img/selected "https://drive.google.com/uc?export=download&id=1DkComby5T3oXJAF979gTeYNfrLRfd7u6" -O equals.png
wget -P $(dirname $0)/img/selected "https://drive.google.com/uc?export=download&id=1a4tkogIUiS6wtQiS2jD-AHaYtZAii3I3" -O division.png
wget -P $(dirname $0)/img/selected "https://drive.google.com/uc?export=download&id=1dC-16t4JJBAt_e5yynQ53t4kQ6QhMzbkATV2" -O addition-sel.png