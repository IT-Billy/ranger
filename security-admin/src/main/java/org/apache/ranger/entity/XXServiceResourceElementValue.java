/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ranger.entity;

import org.apache.ranger.common.AppConstants;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Cacheable
@Table(name = "x_service_resource_element_val")
public class XXServiceResourceElementValue extends XXDBBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "X_SERVICE_RES_EL_VAL_SEQ", sequenceName = "X_SERVICE_RES_EL_VAL_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "X_SERVICE_RES_EL_VAL_SEQ")
    @Column(name = "id")
    protected Long id;

    @Column(name = "res_element_id")
    protected Long resElementId;

    @Column(name = "value")
    protected String value;

    @Column(name = "sort_order")
    protected Integer sortOrder;

    /**
     * @return the resElementId
     */
    public Long getResElementId() {
        return resElementId;
    }

    /**
     * @param resElementId the resElementId to set
     */
    public void setResElementId(Long resElementId) {
        this.resElementId = resElementId;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the sortOrder
     */
    public Integer getSortOrder() {
        return sortOrder;
    }

    /**
     * @param sortOrder the sortOrder to set
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public int getMyClassType() {
        return AppConstants.CLASS_TYPE_XA_SERVICE_RESOURCE_ELEMENT_VALUE;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, resElementId, sortOrder, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!super.equals(obj)) {
            return false;
        }

        XXServiceResourceElementValue other = (XXServiceResourceElementValue) obj;

        return Objects.equals(id, other.id) &&
                Objects.equals(resElementId, other.resElementId) &&
                Objects.equals(sortOrder, other.sortOrder) &&
                Objects.equals(value, other.value);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }

    public StringBuilder toString(StringBuilder sb) {
        sb.append("{ ");
        sb.append(super.toString() + "} ");
        sb.append("id={").append(id).append("} ");
        sb.append("resElementId={").append(resElementId).append("} ");
        sb.append("value={").append(value).append("} ");
        sb.append("sortOrder={").append(sortOrder).append("} ");
        sb.append(" }");

        return sb;
    }
}
