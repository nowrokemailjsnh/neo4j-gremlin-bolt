/*
 *  Copyright 2016 SteelBridge Laboratories, LLC.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  For more information: http://steelbridgelabs.com
 */

package com.steelbridgelabs.oss.neo4j.structure.partitions;

import com.steelbridgelabs.oss.neo4j.structure.Neo4JReadPartition;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This {@link Neo4JReadPartition} implementation creates a {@link org.apache.tinkerpop.gremlin.structure.Graph} partition
 * where all {@link org.apache.tinkerpop.gremlin.structure.Vertex} in graph contain all the partition labels.
 *
 * @author Rogelio J. Baucells
 */
public class AllLabelReadPartition implements Neo4JReadPartition {

    private final Set<String> labels;

    public AllLabelReadPartition(String... labels) {
        Objects.requireNonNull(labels, "labels cannot be null");
        // store labels
        this.labels = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(labels)));
    }

    /**
     * Checks the given label can be added/removed to/from a vertex.
     *
     * @param label The label to validate.
     * @return <code>true</code> if the label can be assigned to a vertex, otherwise <code>false</code>.
     */
    @Override
    public boolean validateLabel(String label) {
        Objects.requireNonNull(label, "label cannot be null");
        // check label is in set
        return !labels.contains(label);
    }

    /**
     * Checks if the partition has the given vertex (labels in vertex). This implementation enforces that all
     * labels in partition must be present in the vertex.
     *
     * @param labels The label to check in the partition.
     * @return <code>true</code> if the vertex is in the partition, otherwise <code>false</code>.
     */
    @Override
    public boolean containsVertex(Set<String> labels) {
        Objects.requireNonNull(labels, "labels cannot be null");
        // all labels must be present in vertex
        return labels.containsAll(this.labels);
    }

    /**
     * Checks if the partition uses MATCH patterns (see {@link #vertexMatchPatternLabels()}).
     *
     * @return <code>true</code> if the partition uses MATCH patterns, otherwise <code>false</code>.
     */
    @Override
    public boolean usesMatchPattern() {
        return true;
    }

    /**
     * Checks if the partition uses MATCH predicate (see {@link #vertexMatchPredicate(String)}).
     *
     * @return <code>true</code> if the partition uses MATCH predicate, otherwise <code>false</code>.
     */
    @Override
    public boolean usesMatchPredicate() {
        return false;
    }

    /**
     * Gets the set of labels required at the time of matching the vertex in a Cypher MATCH pattern. This implementation
     * returns all labels in partition.
     *
     * @return The set of labels.
     */
    @Override
    public Set<String> vertexMatchPatternLabels() {
        return labels;
    }

    /**
     * Generates a {@link org.apache.tinkerpop.gremlin.structure.Vertex} Cypher MATCH predicate, example:
     * <p>
     * (alias:Label1 OR alias:Label2)
     * </p>
     * <p>
     * This implementation just returns <code>null</code> since predicate is not required to match the vertex.
     *
     * @param alias The vertex alias in the MATCH Cypher statement.
     * @return The Cypher MATCH predicate if required by the vertex, otherwise <code>null</code>.
     */
    @Override
    public String vertexMatchPredicate(String alias) {
        return null;
    }
}
