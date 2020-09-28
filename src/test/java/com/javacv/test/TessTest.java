package com.javacv.test;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.leptonica.BOX;
import org.bytedeco.leptonica.BOXA;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.ETEXT_DESC;
import org.bytedeco.tesseract.ResultIterator;
import org.bytedeco.tesseract.TessBaseAPI;
import org.junit.Test;

import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixRead;
import static org.bytedeco.tesseract.global.tesseract.RIL_TEXTLINE;
import static org.bytedeco.tesseract.global.tesseract.RIL_WORD;
import static org.bytedeco.tesseract.global.tesseract.TessMonitorCreate;

/**
 * 图片orc识别
 */
public class TessTest {

    @Test
    public void test1() {

        BytePointer outText;

        TessBaseAPI api = new TessBaseAPI();
        // Initialize tesseract-ocr with English, without specifying tessdata path
        if (api.Init("/Users/jiangfei/IdeaProjects/opencv/tesseract/tessdata", "eng") != 0) {
            System.err.println("Could not initialize tesseract.");
            System.exit(1);
        }

        // Open input image with leptonica library
        PIX image = pixRead("/Users/jiangfei/IdeaProjects/opencv/tesseract/33.jpeg");
        api.SetImage(image);
        // Get OCR result
        outText = api.GetUTF8Text();
        System.out.println("OCR output:\n" + outText.getString());

        // Destroy used object and release memory
        api.End();
        outText.deallocate();
        pixDestroy(image);
    }

    /**
     * result iterator
     */
    @Test
    public void test2() {

        BytePointer outText;

        TessBaseAPI api = new TessBaseAPI();
        // Initialize tesseract-ocr with English, without specifying tessdata path
        if (api.Init("/Users/jiangfei/IdeaProjects/opencv/tesseract/tessdata", "chi_sim_vert") != 0) {
            System.err.println("Could not initialize tesseract.");
            System.exit(1);
        }

        // Open input image with leptonica library
        PIX image = pixRead("/Users/jiangfei/IdeaProjects/opencv/tesseract/22.jpeg");
        api.SetImage(image);

        ETEXT_DESC recoc = TessMonitorCreate();
        api.Recognize(recoc);

        ResultIterator ri = api.GetIterator();
        int pageIteratorLevel = RIL_WORD;
        if (ri != null) {
            do {
                outText = ri.GetUTF8Text(pageIteratorLevel);
                float conf = ri.Confidence(pageIteratorLevel);
                int[] x1 = new int[1], y1 = new int[1], x2 = new int[1], y2 = new int[1];
                ri.BoundingBox(pageIteratorLevel, x1, y1, x2, y2);
                String riInformation = String.format("word: '%s';  \tconf: %.2f; BoundingBox: %d,%d,%d,%d;\n", outText.getString(), conf, x1[0], y1[0], x2[0], y2[0]);
                System.out.println(riInformation);

                outText.deallocate();
            } while (ri.Next(pageIteratorLevel));
        }

        // Destroy used object and release memory
        api.End();
        pixDestroy(image);
    }

    @Test
    public void test3() {

        BytePointer outText;

        TessBaseAPI api = new TessBaseAPI();
        // Initialize tesseract-ocr with English, without specifying tessdata path
        if (api.Init("/Users/jiangfei/IdeaProjects/opencv/tesseract/tessdata", "chi_sim") != 0) {
            System.err.println("Could not initialize tesseract.");
            System.exit(1);
        }

        // Open input image with leptonica library
        PIX image = pixRead("/Users/jiangfei/IdeaProjects/opencv/tesseract/11.png");
        api.SetImage(image);


        // Lookup all component images
        int[] blockIds = {};
        BOXA boxes = api.GetComponentImages(RIL_TEXTLINE, true, null, blockIds);

        for (int i = 0; i < boxes.n(); i++) {
            // For each image box, OCR within its area
            BOX box = boxes.box(i);
            api.SetRectangle(box.x(), box.y(), box.w(), box.h());
            outText = api.GetUTF8Text();
            String ocrResult = outText.getString();
            int conf = api.MeanTextConf();

            String boxInformation = String.format("Box[%d]: x=%d, y=%d, w=%d, h=%d, confidence: %d, text: %s", i, box.x(), box.y(), box.w(), box.h(), conf, ocrResult);
            System.out.println(boxInformation);

            outText.deallocate();
        }

        // Destroy used object and release memory
        api.End();
        pixDestroy(image);

    }

}
