package edu.fmi.sudo.deduplicator.entities;

import java.util.List;

/**
 * @author Miroslav Kramolinski
 *
 */
public interface Question {
    String getId();
    String getSubject();
    String getBody();
    List<String> getTokens();
}
