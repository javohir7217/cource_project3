package com.appliance.store.dal.datasource;

import com.appliance.store.constant.CsvSchemaConstants;
import com.appliance.store.constant.DelimiterConstants;
import com.appliance.store.constant.ErrorMessageConstants;
import com.appliance.store.dal.exception.DaoException;
import com.appliance.store.entity.BaseEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvDataSource<T extends BaseEntity> implements DataSource<T> {
    private final String resourcePath;
    private final CsvRowMapper<T> rowMapper;

    public CsvDataSource(String resourcePath, CsvRowMapper<T> rowMapper) {
        if (resourcePath == null) {
            throw new IllegalArgumentException(ErrorMessageConstants.FILE_NOT_FOUND_OR_UNREADABLE_PREFIX + null);
        }
        if (rowMapper == null) {
            throw new IllegalArgumentException(ErrorMessageConstants.DATA_SOURCE_CANNOT_BE_NULL);
        }
        this.resourcePath = resourcePath;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> readAll() throws DaoException {
        InputStream is = openInputStream(resourcePath);
        if (is == null) {
            throw new DaoException(ErrorMessageConstants.FILE_NOT_FOUND_OR_UNREADABLE_PREFIX + resourcePath);
        }

        List<T> entities = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (isFirstLine) {
                    isFirstLine = false;
                    if (isHeaderLine(line)) {
                        continue;
                    }
                }
                String[] columns = parseCsvLine(line);
                T entity = rowMapper.mapRow(columns);
                entities.add(entity);
            }
        } catch (IOException e) {
            throw new DaoException(ErrorMessageConstants.FILE_NOT_FOUND_OR_UNREADABLE_PREFIX + resourcePath, e);
        }
        return Collections.unmodifiableList(entities);
    }

    private InputStream openInputStream(String path) {
        String cleanPath = path.startsWith(DelimiterConstants.SLASH)
                ? path.substring(DelimiterConstants.ONE_OFFSET)
                : path;

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(cleanPath);
        if (is != null) {
            return is;
        }
        is = getClass().getClassLoader().getResourceAsStream(cleanPath);
        if (is != null) {
            return is;
        }
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try {
                return new FileInputStream(file);
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private boolean isHeaderLine(String line) {
        return line.startsWith(CsvSchemaConstants.HEADER_ID + DelimiterConstants.CSV_DELIMITER);
    }

    private String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = DelimiterConstants.ZERO_INDEX; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == DelimiterConstants.CHAR_QUOTE) {
                insideQuotes = !insideQuotes;
            } else if (c == DelimiterConstants.CHAR_COMMA && !insideQuotes) {
                tokens.add(cleanToken(sb.toString()));
                sb.setLength(DelimiterConstants.ZERO_INDEX);
            } else {
                sb.append(c);
            }
        }
        tokens.add(cleanToken(sb.toString()));
        return tokens.toArray(DelimiterConstants.EMPTY_STRING_ARRAY);
    }

    private String cleanToken(String token) {
        token = token.trim();
        if (token.startsWith(DelimiterConstants.QUOTE) && token.endsWith(DelimiterConstants.QUOTE)
                && token.length() >= DelimiterConstants.MIN_QUOTED_LENGTH) {
            token = token.substring(DelimiterConstants.ONE_OFFSET, token.length() - DelimiterConstants.ONE_OFFSET)
                    .trim();
        }
        return token;
    }
}