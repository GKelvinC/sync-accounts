package br.com.sicredi.sincronizacao.infra.file.processor.csv.helpers;

import com.opencsv.bean.CsvBindByName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@Component
public class CSVMapperHelper<T,R> {

    public T mapRecordToBean(String[] record, String[] header, Class<T> clazz) {
        try {
            T bean = clazz.getDeclaredConstructor().newInstance();
            Map<String, String> headerToFieldMap = getHeaderToFieldMap(header, record);

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(CsvBindByName.class)) {
                    CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
                    String columnName = annotation.column();
                    String fieldValue = headerToFieldMap.get(columnName);

                    if (fieldValue != null) {
                        field.setAccessible(true);
                        field.set(bean, convertValue(field, fieldValue));
                    }
                }
            }
            return bean;
        } catch (Exception e) {
            log.error("Erro ao mapear o registro para o bean: ", e);
            return null;
        }
    }

    public String mapBeanToRecord(R bean, String[] header,String csvSplitter) {
        String[] record = new String[header.length];
        try {
            Map<String, String> valueMap = new HashMap<>();

            Class<?> current = bean.getClass();
            List<Class<?>> classes = new ArrayList<>();
            while (current.getSuperclass() != null) {
                classes.add(current);
                current = current.getSuperclass();
            }
            classes.forEach(aClass -> {
                for (Field field : aClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(CsvBindByName.class)) {
                        field.setAccessible(true);
                        CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
                        String columnName = annotation.column();
                        Object value;
                        try {
                            value = field.get(bean);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        valueMap.put(columnName, value != null ? value.toString() : "");
                    }
                }
            });
            for (int i = 0; i < header.length; i++) {
                record[i] = valueMap.getOrDefault(header[i], "");
            }

        } catch (Exception e) {
            log.error("Erro ao mapear o bean para o registro: ", e);
        }
        return String.join(String.valueOf(csvSplitter.charAt(0)), record) + "\n";
    }

    private Map<String, String> getHeaderToFieldMap(String[] header, String[] record) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < header.length; i++) {
            map.put(header[i], record[i]);
        }
        return map;
    }

    private Object convertValue(Field field, String value) {
        if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
            return Long.parseLong(value);
        } else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
            return Double.parseDouble(value);
        } else if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        }
        return value;
    }
}