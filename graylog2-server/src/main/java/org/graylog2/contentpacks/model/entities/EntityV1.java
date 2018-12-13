/**
 * This file is part of Graylog.
 *
 * Graylog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graylog2.contentpacks.model.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.NullNode;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import org.graylog2.contentpacks.model.ContentPack;
import org.graylog2.contentpacks.model.ModelId;
import org.graylog2.contentpacks.model.ModelTypes;
import org.graylog2.contentpacks.model.ModelVersion;
import org.graylog2.contentpacks.model.constraints.Constraint;
import org.graylog2.contentpacks.model.constraints.GraylogVersionConstraint;

import java.util.UUID;

@AutoValue
@JsonAutoDetect
@JsonDeserialize(builder = AutoValue_EntityV1.Builder.class)
public abstract class EntityV1 implements Entity {
    public static final String VERSION = "1";
    public static final String FIELD_DATA = "data";
    public static final String FIELD_CONSTRAINTS = "constraints";

    // TODO: Use more type-safe way to represent entity configuration?
    @JsonProperty(FIELD_DATA)
    public abstract JsonNode data();

    @JsonProperty(FIELD_CONSTRAINTS)
    public abstract ImmutableSet<Constraint> constraints();

    @Override
    public EntityDescriptor toEntityDescriptor() {
        return EntityDescriptor.builder()
                .id(id())
                .type(type())
                .build();
    }

    public static Builder builder() {
        final ImmutableSet constraints = ImmutableSet.<Constraint>builder()
                .add(GraylogVersionConstraint.currentGraylogVersion()).build();
        return new AutoValue_EntityV1.Builder()
                /* TODO: KM: should be removed at the end */
                .constraints(constraints)
                .id(ModelId.of(UUID.randomUUID().toString()));
    }

    public static Entity createRoot(ContentPack contentPack) {
        return EntityV1.builder()
                .type(ModelTypes.ROOT)
                .id(ModelId.of("virtual-root-" + contentPack.id() + "-" + contentPack.revision()))
                .data(NullNode.getInstance())
                .constraints(ImmutableSet.of())
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder implements EntityBuilder<Builder> {
        @JsonProperty(FIELD_DATA)
        public abstract Builder data(JsonNode data);

        @JsonProperty(FIELD_CONSTRAINTS)
        public abstract Builder constraints(ImmutableSet<Constraint> constraints);

        abstract EntityV1 autoBuild();

        public EntityV1 build() {
            version(ModelVersion.of(VERSION));
            return autoBuild();
        }
    }
}
