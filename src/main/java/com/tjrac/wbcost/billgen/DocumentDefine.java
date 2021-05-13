package com.tjrac.wbcost.billgen;

public class DocumentDefine {
    private static final String documentHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<w:document\n" +
            "    xmlns:wpc=\"http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas\"\n" +
            "    xmlns:cx=\"http://schemas.microsoft.com/office/drawing/2014/chartex\"\n" +
            "    xmlns:cx1=\"http://schemas.microsoft.com/office/drawing/2015/9/8/chartex\"\n" +
            "    xmlns:cx2=\"http://schemas.microsoft.com/office/drawing/2015/10/21/chartex\"\n" +
            "    xmlns:cx3=\"http://schemas.microsoft.com/office/drawing/2016/5/9/chartex\"\n" +
            "    xmlns:cx4=\"http://schemas.microsoft.com/office/drawing/2016/5/10/chartex\"\n" +
            "    xmlns:cx5=\"http://schemas.microsoft.com/office/drawing/2016/5/11/chartex\"\n" +
            "    xmlns:cx6=\"http://schemas.microsoft.com/office/drawing/2016/5/12/chartex\"\n" +
            "    xmlns:cx7=\"http://schemas.microsoft.com/office/drawing/2016/5/13/chartex\"\n" +
            "    xmlns:cx8=\"http://schemas.microsoft.com/office/drawing/2016/5/14/chartex\"\n" +
            "    xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\"\n" +
            "    xmlns:aink=\"http://schemas.microsoft.com/office/drawing/2016/ink\"\n" +
            "    xmlns:am3d=\"http://schemas.microsoft.com/office/drawing/2017/model3d\"\n" +
            "    xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n" +
            "    xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"\n" +
            "    xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"\n" +
            "    xmlns:v=\"urn:schemas-microsoft-com:vml\"\n" +
            "    xmlns:wp14=\"http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing\"\n" +
            "    xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\"\n" +
            "    xmlns:w10=\"urn:schemas-microsoft-com:office:word\"\n" +
            "    xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"\n" +
            "    xmlns:w14=\"http://schemas.microsoft.com/office/word/2010/wordml\"\n" +
            "    xmlns:w15=\"http://schemas.microsoft.com/office/word/2012/wordml\"\n" +
            "    xmlns:w16cid=\"http://schemas.microsoft.com/office/word/2016/wordml/cid\"\n" +
            "    xmlns:w16se=\"http://schemas.microsoft.com/office/word/2015/wordml/symex\"\n" +
            "    xmlns:wpg=\"http://schemas.microsoft.com/office/word/2010/wordprocessingGroup\"\n" +
            "    xmlns:wpi=\"http://schemas.microsoft.com/office/word/2010/wordprocessingInk\"\n" +
            "    xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\"\n" +
            "    xmlns:wps=\"http://schemas.microsoft.com/office/word/2010/wordprocessingShape\" mc:Ignorable=\"w14 w15 w16se w16cid wp14\">\n" +
            "    <w:body>\n" +
            "        <w:p w:rsidR=\"001430F8\" w:rsidRDefault=\"001A7792\" w:rsidP=\"001A7792\">\n" +
            "            <w:pPr>\n" +
            "                <w:jc w:val=\"center\"/>\n" +
            "                <w:rPr>\n" +
            "                    <w:sz w:val=\"30\"/>\n" +
            "                    <w:szCs w:val=\"30\"/>\n" +
            "                </w:rPr>\n" +
            "            </w:pPr>\n" +
            "            <w:r w:rsidRPr=\"001A7792\">\n" +
            "                <w:rPr>\n" +
            "                    <w:rFonts w:hint=\"eastAsia\"/>\n" +
            "                    <w:sz w:val=\"30\"/>\n" +
            "                    <w:szCs w:val=\"30\"/>\n" +
            "                </w:rPr>\n" +
            "                <w:t>消费</w:t>\n" +
            "            </w:r>\n" +
            "            <w:r>\n" +
            "                <w:rPr>\n" +
            "                    <w:rFonts w:hint=\"eastAsia\"/>\n" +
            "                    <w:sz w:val=\"30\"/>\n" +
            "                    <w:szCs w:val=\"30\"/>\n" +
            "                </w:rPr>\n" +
            "                <w:t>清单</w:t>\n" +
            "            </w:r>\n" +
            "        </w:p>\n" +
            "        <w:p w:rsidR=\"001A7792\" w:rsidRPr=\"001A7792\" w:rsidRDefault=\"001A7792\" w:rsidP=\"001A7792\">\n" +
            "            <w:pPr>\n" +
            "                <w:jc w:val=\"center\"/>\n" +
            "                <w:rPr>\n" +
            "                    <w:sz w:val=\"30\"/>\n" +
            "                    <w:szCs w:val=\"30\"/>\n" +
            "                </w:rPr>\n" +
            "            </w:pPr>\n" +
            "        </w:p>";
    private static final String documentSpace = "<w:p w:rsidR=\"00E435DB\" w:rsidRDefault=\"00E435DB\" w:rsidP=\"00ED1816\">\n" +
            "            <w:pPr>\n" +
            "                <w:jc w:val=\"center\"/>\n" +
            "            </w:pPr>\n" +
            "        </w:p>";

    private static final String documentFooter = "<w:p w:rsidR=\"00E435DB\" w:rsidRPr=\"00ED1816\" w:rsidRDefault=\"00E435DB\" w:rsidP=\"00E435DB\">\n" +
            "            <w:pPr>\n" +
            "                <w:jc w:val=\"center\"/>\n" +
            "                <w:rPr>\n" +
            "                    <w:rFonts w:hint=\"eastAsia\"/>\n" +
            "                </w:rPr>\n" +
            "            </w:pPr>\n" +
            "            <w:r>\n" +
            "                <w:rPr>\n" +
            "                    <w:rFonts w:hint=\"eastAsia\"/>\n" +
            "                </w:rPr>\n" +
            "                <w:t>欢迎再次光临！</w:t>\n" +
            "            </w:r>\n" +
            "            <w:bookmarkStart w:id=\"0\" w:name=\"_GoBack\"/>\n" +
            "            <w:bookmarkEnd w:id=\"0\"/>\n" +
            "        </w:p>\n" +
            "        <w:sectPr w:rsidR=\"00E435DB\" w:rsidRPr=\"00ED1816\" w:rsidSect=\"00DE51D5\">\n" +
            "            <w:pgSz w:w=\"11900\" w:h=\"16840\"/>\n" +
            "            <w:pgMar w:top=\"1440\" w:right=\"1800\" w:bottom=\"1440\" w:left=\"1800\" w:header=\"851\" w:footer=\"992\" w:gutter=\"0\"/>\n" +
            "            <w:cols w:space=\"425\"/>\n" +
            "            <w:docGrid w:type=\"lines\" w:linePitch=\"312\"/>\n" +
            "        </w:sectPr>\n" +
            "    </w:body>\n" +
            "</w:document>";


    private static final String textModel1 = "<w:p w:rsidR=\"001A7792\" w:rsidRDefault=\"001A7792\" w:rsidP=\"001A7792\">\n" +
            "            <w:pPr>\n" +
            "                <w:jc w:val=\"center\"/>\n" +
            "            </w:pPr>\n" +
            "            <w:r>\n" +
            "                <w:rPr>\n" +
            "                    <w:rFonts w:hint=\"eastAsia\"/>\n" +
            "                </w:rPr>\n" +
            "                <w:t>";
    private static final String textModel2 = "</w:t></w:r></w:p>";

    /**
     * 生成XML文档
     *
     * @param texts  待填写的文字，每个元素一行
     * @param texts2 同text1，输出时会与text1间隔一行
     * @return
     */
    public static String generateWord(String[] texts, String[] texts2) {
        StringBuilder sb = new StringBuilder();
        sb.append(documentHeader);
        if (texts != null) {
            for (String text : texts) {
                sb.append(textModel1);
                sb.append(text);
                sb.append(textModel2);
            }
        }
        sb.append(documentSpace);
        if (texts2 != null) {
            for (String text : texts2) {
                sb.append(textModel1);
                sb.append(text);
                sb.append(textModel2);
            }
        }
        sb.append(documentSpace);
        sb.append(documentFooter);
        return sb.toString();
    }
}
