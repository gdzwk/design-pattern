package zwk.util.poi;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

/**
 * excel 样式设定
 * @author zwk
 */
public class PoiStyle {

    /**
     * 标题样式
     * @param sheet
     *                  工作表
     * @return 标题样式
     */
    public static CellStyle titleStyle(Sheet sheet) {
        Workbook workbook = sheet.getWorkbook();
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillPattern(FillPatternType.FINE_DOTS);
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LEMON_CHIFFON.getIndex());
        return style;
    }

    /**
     * 列名样式
     * @param sheet
     *                  工作表
     * @return 列名样式
     */
    public static CellStyle colStyle(Sheet sheet) {
        Workbook workbook = sheet.getWorkbook();
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillPattern(FillPatternType.FINE_DOTS);
        style.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
        return style;
    }

}
