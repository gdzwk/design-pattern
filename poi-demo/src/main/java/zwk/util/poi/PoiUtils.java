package zwk.util.poi;

import com.zwk.util.lang.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * excel工具类
 * @author zwk
 */
public class PoiUtils {

    public static ExportBean createExport() {
        return new ExportBean();
    }

    public static class ExportBean {
        private XSSFWorkbook xssfWorkbook;
        private int sheetNameCount;
        private Set<String> sheetSet;
        private SimpleDateFormat sdf;

        private static final String DEFAULT_SHEET_NAME = "sheet";
        private static final String DEFAULT_COLUMN_NAME = "col_";

        public ExportBean() {
            this.xssfWorkbook = new XSSFWorkbook();
            this.sheetNameCount = 0;
            this.sheetSet = new HashSet<String>(8);
            this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        /**
         * 把集合写入excel sheet
         * @param clazz 值对象类型
         * @param list 需要写入excel的集合
         */
        public <T> ExportBean createSheet(Class<T> clazz, List<T> list) {
            return this.createSheet(clazz, list, null);
        }

        public void output(String filePath) {
            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                this.xssfWorkbook.write(new BufferedOutputStream(new FileOutputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 把集合写入excel sheet
         * @param clazz 值对象类型
         * @param list 需要写入excel的集合
         * @param sheetName sheet名称
         */
        public <T> ExportBean createSheet(Class<T> clazz, List<T> list, @Nullable String sheetName) {
            Sheet sheet = this.xssfWorkbook.createSheet(this.validateSheetName(sheetName));
            Field[] fields = clazz.getDeclaredFields();
            // 行开始索引
            int hIndex = writeTitles(sheet, fields);
            int colIndex = 0;
            Row row;
            Cell cell;
            try {
                for (Field field : fields) {
                    if (! field.isAnnotationPresent(Exp.class)) {
                        continue;
                    }

                    Exp exp = field.getAnnotation(Exp.class);
                    assert exp.width() <= 256;
                    sheet.setColumnWidth(colIndex, exp.width() * 256);

                    int rowIndex = hIndex;
                    for (T t : list) {
                        if (colIndex == hIndex) {
                            row = sheet.createRow(rowIndex++);
                        } else {
                            row = sheet.getRow(rowIndex++);
                        }

                        cell = row.createCell(colIndex);
                        if ("Boolean".equalsIgnoreCase(field.getType().getSimpleName())) {
                            cell.setCellType(CellType.BOOLEAN);
                            cell.setCellValue(field.getBoolean(t));
                        } else if ("String".equalsIgnoreCase(field.getType().getSimpleName())) {
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(field.get(t).toString());
                        } else if ("Integer".equalsIgnoreCase(field.getType().getSimpleName())) {
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellValue(field.getInt(t));
                        } else if ("Long".equalsIgnoreCase(field.getType().getSimpleName())) {
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellValue(field.getLong(t));
                        } else if ("Double".equalsIgnoreCase(field.getType().getSimpleName())) {
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellValue(field.getDouble(t));
                        } else if ("Date".equalsIgnoreCase(field.getType().getSimpleName())) {
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(sdf.format((Date) field.get(t)));
                        }
                    }

                    colIndex++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        /**
         * 校验sheet名称是否重复，如重复则给一个新值
         */
        private String validateSheetName(@Nullable String sheetName) {
            if (StringUtils.isEmpty(sheetName)) {
                sheetName = DEFAULT_SHEET_NAME + sheetNameCount++;
            }
            while (sheetSet.contains(sheetName)) {
                sheetName = DEFAULT_SHEET_NAME + sheetNameCount++;
            }
            sheetSet.add(sheetName);
            return sheetName;
        }

        /**
         * 设置标题与列名
         * @param sheet 工作表
         * @param fields 属性集合
         * @return 行索引
         */
        private static <T> int writeTitles(Sheet sheet, Field[] fields) {
            int rowIndex = 0;
            Row row;
            Cell cell;
            // 列名
            List<String> columnNames = getColumnNames(fields);
            // 设置标题
            row = sheet.createRow(rowIndex);
            cell = row.createCell(0);
            cell.setCellValue(sheet.getSheetName());
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, columnNames.size() > 0 ? columnNames.size() -  1 : 0));
            cell.setCellStyle(PoiStyle.titleStyle(sheet));
            // 设置列名
            row = sheet.createRow(++rowIndex);
            for (int i = 0; i < columnNames.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(columnNames.get(i));
                cell.setCellStyle(PoiStyle.colStyle(sheet));
            }
            return ++rowIndex;
        }

        /**
         * 获取列名称
         */
        private static List<String> getColumnNames(Field[] fields) {
            List<String> columnNames = new ArrayList<String>(16);

            int col = 0;
            for (Field field : fields) {
                if (field.isAnnotationPresent(Exp.class)) {
                    Exp exp = field.getAnnotation(Exp.class);
                    columnNames.add(StringUtils.isEmpty(exp.name()) ? DEFAULT_COLUMN_NAME + col++ : exp.name());
                }
            }
            if (columnNames.size() <= 0) {
                throw new RuntimeException("PoiUtil.getTitle error: no title available.");
            }
            return columnNames;
        }

    }

}
