set terminal svg size 920,920 
set output 'generatedFiles/gnuplot/twoGaussians.svg'
set title "histo" 
set grid
set style data points
plot'../graphs/twoGaussians.d'