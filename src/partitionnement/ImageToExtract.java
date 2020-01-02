package partitionnement;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ImageToExtract {
    String imageFileName;
    String imageSourcePath;
    String dataSourcePath;
    String gnuplotSourcePath;

    int width;
    int height;

    FileWriter data;
    BufferedImage bi;
    static double twoFiveFiveInvert = 1/255.f;

    public ImageToExtract(String imageFileName,String imageSourcePath,String dataSourcePath,String gnuplotSourcePath) throws IOException {
        this.imageFileName = imageFileName;
        this.imageSourcePath = imageSourcePath;
        this.dataSourcePath = dataSourcePath;
        bi = ImageIO.read(new File(imageSourcePath+imageFileName+".png"));
        width = bi.getWidth();
        height = bi.getHeight();
        data = new FileWriter(dataSourcePath + imageFileName + ".d");
        this.gnuplotSourcePath = gnuplotSourcePath;
    }
    public ImageToExtract(String imageFileName) throws IOException {
        this(imageFileName,"pictures/","generatedFiles/data/","generatedFiles/gnuplot/");
    }

    /**
     * @return : creates normalized dataPoints in a .d file and makes the double[][] the learning algo needs.
     */
    public double[][] extractAllPixelsColorNormalized() throws IOException {
        int[] im_pixels = bi.getRGB(0, 0, width, height, null, 0, width);
        double[][] points = new double[im_pixels.length][3];
        Color nowColor;
        for (int pixelIdx = 0; pixelIdx < im_pixels.length; pixelIdx+=1) {
            nowColor = new Color(im_pixels[pixelIdx]);
            points[pixelIdx][0] = nowColor.getRed() * twoFiveFiveInvert;
            points[pixelIdx][1] = nowColor.getGreen() * twoFiveFiveInvert;
            points[pixelIdx][2] = nowColor.getBlue() * twoFiveFiveInvert;
            data.write("" + points[pixelIdx][0] + " " + points[pixelIdx][1] + " " + points[pixelIdx][2]
                    + " " + nowColor.getRed() + " " + nowColor.getGreen() + " " + nowColor.getBlue() + "\n");
        }
        return points;
    }

    public void createGnuPlot() throws IOException {
        FileWriter gnuplot = new FileWriter(gnuplotSourcePath+imageFileName+".gnu");
        gnuplot.write("rgb(r,g,b) = int(65536 * r) + int(256 * g) + int(b)\n");
        gnuplot.write("splot \""+ "../data/" + imageFileName + ".d\" using 1:2:3:(rgb($4,$5,$6)) with points lc rgb variable");
        gnuplot.close();
    }
    public static void main(String[] args) throws IOException
    {
        ImageToExtract mms = new ImageToExtract("mms");
        mms.extractAllPixelsColorNormalized();
        mms.createGnuPlot();
    }
}
