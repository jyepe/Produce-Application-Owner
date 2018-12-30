package com.example.yepej.greenseasons;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelInvoice
{
    private WritableWorkbook wb;
    InstanceInfo info;
    private String[] itemList;
    private String orderID;
    private String[] orderDetailList;

    public ExcelInvoice(WritableWorkbook wb, String orderID, String[] orderDetailList)
    {
        info = InstanceInfo.getInstance();
        this.wb = wb;
        this.orderID = orderID;
        this.orderDetailList = orderDetailList;
        createInvoice();
    }

    private void createInvoice()
    {
        try
        {
            WritableSheet sheet = wb.createSheet("Green Seasons", 0);
            setColumnsWidth(sheet);

            addStaticCells(sheet);


            addItemsToInvoice(sheet);
            addFormattedCells(sheet);

            wb.write();
            wb.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (RowsExceededException e)
        {
            e.printStackTrace();
        }
        catch (WriteException e)
        {
            e.printStackTrace();
        }
    }

    //region Edit sheet
    private void addStaticCells(WritableSheet sheet) throws WriteException
    {
        Label heading = new Label(3, 0, "GREEN SEASONS", headerFormat());
        Label qty = new Label(0, 4, "Qty", descriptionFormat());
        Label description = new Label(1, 4, "Description", descriptionFormat());
        Label price = new Label(2, 4, "Price", descriptionFormat());
        Label amount = new Label(3, 4, "Amount", descriptionFormat());

        Label qty2 = new Label(5, 4, "Qty", descriptionFormat());
        Label description2 = new Label(6, 4, "Description", descriptionFormat());
        Label price2 = new Label(7, 4, "Price", descriptionFormat());
        Label amount2 = new Label(8, 4, "Amount", descriptionFormat());

        Label receivedBy = new Label(0,40,"Received By:_______________");
        Label soldTo = new Label(0,41,"Sold To:_______________");
        Label date = new Label(0,42,"Date:_______________");
        Label total = new Label(0,43,"Total:__________");

        sheet.addCell(heading);
        sheet.addCell(qty);
        sheet.addCell(description);
        sheet.addCell(price);
        sheet.addCell(amount);
        sheet.addCell(qty2);
        sheet.addCell(description2);
        sheet.addCell(price2);
        sheet.addCell(amount2);
        sheet.addCell(receivedBy);
        sheet.addCell(soldTo);
        sheet.addCell(date);
        sheet.addCell(total);
    }

    private void setColumnsWidth(WritableSheet sheet)
    {
        sheet.setColumnView(0,4);
        sheet.setColumnView(1,22);
        sheet.setColumnView(2,5);
        sheet.setColumnView(3,8);
        sheet.setColumnView(5,4);
        sheet.setColumnView(6,17);
        sheet.setColumnView(7,6);
        sheet.setColumnView(8,8);
    }


    private void addItemsToInvoice(WritableSheet sheet)
    {
        getItems();
        setQtyAndPrice(sheet);
        setDescription(sheet);
    }

    private void setDescription(WritableSheet sheet)
    {
        int count = 0;

        for (int i = 5; i < 61; i++)
        {
            try
            {
                if (i > 37)
                {
                    Label cell = new Label(6,i - 33,itemList[count]);
                    sheet.addCell(cell);
                }
                else
                {
                    Label cell = new Label(1,i,itemList[count]);
                    sheet.addCell(cell);
                }
            }
            catch (WriteException e)
            {
                e.printStackTrace();
            }
            count++;
        }
    }

    private void setQtyAndPrice(WritableSheet sheet)
    {
        for (int i = 0; i < orderDetailList.length; i++)
        {
            for (int j = 0; j < itemList.length; j++)
            {
                if (orderDetailList[i].split(",")[0].equalsIgnoreCase(itemList[j]))
                {
                    if (j >= 37)
                    {
                        Label qty = new Label(5,j - 28,orderDetailList[i].split(",")[1]);
                        Label price = new Label(7,j - 28, orderDetailList[i].split(",")[2]);
                        Label total = new Label(8,j - 28, calculateTotal(i));
                        try
                        {
                            sheet.addCell(qty);
                            sheet.addCell(price);
                            sheet.addCell(total);
                        }
                        catch (WriteException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Label qty = new Label(0,j + 5, orderDetailList[i].split(",")[1]);
                        Label price = new Label(2,j + 5, orderDetailList[i].split(",")[2]);
                        Label total = new Label(3,j + 5, calculateTotal(i));
                        try
                        {
                            sheet.addCell(qty);
                            sheet.addCell(price);
                            sheet.addCell(total);
                        }
                        catch (WriteException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private String calculateTotal(int index)
    {
        double qty = Double.parseDouble(orderDetailList[index].split(",")[1]);
        double price = Double.parseDouble(orderDetailList[index].split(",")[2]);

        double total = Math.round(qty * price * 100.0) / 100.0;

        return String.valueOf(total);
    }


    private void addFormattedCells(WritableSheet sheet) throws WriteException
    {
        for (int i = 0; i < itemList.length; i++)
        {
            if (i > 32)
            {
                Label qtyLabel = new Label(5,i - 28,sheet.getWritableCell(5,i - 28).getContents(), addAllBorders());
                Label descriptionLabel = new Label(6,i - 28,sheet.getWritableCell(6,i - 28).getContents(), addAllBorders());
                Label priceLabel = new Label(7,i - 28, sheet.getWritableCell(7,i - 28).getContents(), addAllBorders());
                Label amountLabel = new Label(8,i - 28, sheet.getWritableCell(8,i - 28).getContents(), addAllBorders());
                sheet.addCell(descriptionLabel);
                sheet.addCell(amountLabel);
                sheet.addCell(priceLabel);
                sheet.addCell(qtyLabel);
            }
            else
            {
                Label qtyLabel = new Label(0, i + 5, sheet.getWritableCell(0, i + 5).getContents(), addAllBorders());
                Label descriptionLabel = new Label(1,i + 5,sheet.getWritableCell(1,i + 5).getContents(), addAllBorders());
                Label priceLabel = new Label(2,i + 5, sheet.getWritableCell(2,i + 5).getContents(), addAllBorders());
                Label amountLabel = new Label(3,i + 5, sheet.getWritableCell(3,i + 5).getContents(), addAllBorders());
                sheet.addCell(descriptionLabel);
                sheet.addCell(amountLabel);
                sheet.addCell(priceLabel);
                sheet.addCell(qtyLabel);
            }


        }
    }
    //endregion

    //region Contact server
    //Gets all inventory items from DB
    private void getItems()
    {
        PostSender sendPostData = new PostSender();

        try
        {
            String data = URLEncoder.encode("method", info.getEncodeFormat()) + "=" +
                    URLEncoder.encode("getItems", info.getEncodeFormat());
            data += "&" + URLEncoder.encode("type", info.getEncodeFormat()) + "=" +
                    URLEncoder.encode("", info.getEncodeFormat());

            String serverResponse = sendPostData.execute("http://" + info.getServerIP() + "/ds.php", data).get();
            parseResponse(serverResponse);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    //Puts the data returned into an array
    private void parseResponse(String serverResponse)
    {
        //Searches for everything between start and end of the server response
        Pattern p = Pattern.compile("start(.*?)end");
        Matcher m = p.matcher(serverResponse);

        while (m.find())
        {
            itemList = m.group(1).split(",");
        }
    }
    //endregion

    //region Cell formats
    private CellFormat descriptionFormat()
    {
        WritableFont font = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD,
                true, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);

        WritableCellFormat format = new WritableCellFormat(font);


        try
        {
            format.setBackground(Colour.DARK_GREEN);
        }
        catch (WriteException e)
        {
            e.printStackTrace();
        }

        return format;
    }

    private CellFormat headerFormat()
    {
        WritableFont font = new WritableFont(WritableFont.TIMES, 24, WritableFont.NO_BOLD,
                true, UnderlineStyle.NO_UNDERLINE, Colour.DARK_GREEN);

        WritableCellFormat format = new WritableCellFormat(font);

        return format;
    }

    private CellFormat addAllBorders()
    {
        WritableCellFormat borderFormat = new WritableCellFormat();

        try
        {
            borderFormat.setBorder(Border.ALL, BorderLineStyle.THICK,Colour.DARK_GREEN);
        }
        catch (WriteException e)
        {
            e.printStackTrace();
        }

        return borderFormat;
    }
    //endregion
}
