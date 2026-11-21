package com.appliance.store.controller.pipeline;

import com.appliance.store.constant.DelimiterConstants;
import com.appliance.store.controller.model.ParsedRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestParser {
    private static final Pattern TOKEN_PATTERN = Pattern.compile("([^\\s=]+)=(?:\"([^\"]*)\"|(\\S+))|\"([^\"]*)\"|(\\S+)");

    public ParsedRequest parse(String requestString) {
        if (requestString == null || requestString.isBlank()) {
            return new ParsedRequest(DelimiterConstants.EMPTY, null, Collections.emptyList());
        }

        Matcher matcher = TOKEN_PATTERN.matcher(requestString.trim());
        List<String> rawTokens = new ArrayList<>();

        while (matcher.find()) {
            if (matcher.group(DelimiterConstants.REGEX_GROUP_ONE) != null) {
                String key = matcher.group(DelimiterConstants.REGEX_GROUP_ONE);
                String val = matcher.group(DelimiterConstants.REGEX_GROUP_TWO) != null
                        ? matcher.group(DelimiterConstants.REGEX_GROUP_TWO)
                        : matcher.group(DelimiterConstants.REGEX_GROUP_THREE);
                rawTokens.add(key + DelimiterConstants.KEY_VALUE_DELIMITER + val);
            } else if (matcher.group(DelimiterConstants.REGEX_GROUP_FOUR) != null) {
                rawTokens.add(matcher.group(DelimiterConstants.REGEX_GROUP_FOUR));
            } else if (matcher.group(DelimiterConstants.REGEX_GROUP_FIVE) != null) {
                rawTokens.add(matcher.group(DelimiterConstants.REGEX_GROUP_FIVE));
            }
        }

        if (rawTokens.isEmpty()) {
            return new ParsedRequest(DelimiterConstants.EMPTY, null, Collections.emptyList());
        }

        String commandName = rawTokens.get(DelimiterConstants.ZERO_INDEX);
        String targetName = rawTokens.size() > DelimiterConstants.ONE_INDEX ? rawTokens.get(DelimiterConstants.ONE_INDEX) : null;
        List<String> params = rawTokens.size() > DelimiterConstants.TWO_INDEX
                ? rawTokens.subList(DelimiterConstants.TWO_INDEX, rawTokens.size())
                : Collections.emptyList();

        return new ParsedRequest(commandName, targetName, params);
    }
}

