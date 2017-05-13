package com.aiton.administrator.shane_library.shane.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by Administrator on 2016/2/3.
 */
public  class SubJsonStr
{
    public static String dislodgeHeadTag(String str)
    {
        String fanilResult;
        Document doc = null;
        try
        {
            doc = DocumentHelper.parseText(str);
            Element testElement = doc.getRootElement();
            String testxml = testElement.asXML();
            fanilResult = testxml.substring(testxml.indexOf(">") + 1, testxml.lastIndexOf("<"));
            return fanilResult;
        } catch (DocumentException e)
        {
            e.printStackTrace();
        }
        return "解析失败";
    }
}
