rgb(r,g,b) = int(65536 * r) + int(256 * g) + int(b)
splot "../data/peacock.d" using 1:2:3:(rgb($4,$5,$6)) with points lc rgb variable,\
      "../data/peacockCenters.d" using 1:2:3:4:(rgb($5,$5,$5)) pt 7 ps variable lc rgb variable