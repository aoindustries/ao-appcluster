/*
 * ao-appcluster - Coordinates system components installed in master/slave replication.
 * Copyright (C) 2011  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of ao-appcluster.
 *
 * ao-appcluster is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ao-appcluster is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ao-appcluster.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aoindustries.appcluster;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.xbill.DNS.Name;

/**
 * The node settings on a per-resource basis.
 *
 * @author  AO Industries, Inc.
 */
abstract public class ResourceNode<R extends Resource<R,RN>,RN extends ResourceNode<R,RN>> {

    private final Node node;
    private final Set<Name> nodeRecords;
    private R resource;

    ResourceNode(Node node, AppClusterConfiguration.ResourceNodeConfiguration resourceNodeConfiguration) {
        this.node = node;
        this.nodeRecords = Collections.unmodifiableSet(new LinkedHashSet<Name>(resourceNodeConfiguration.getNodeRecords()));
    }
    void init(R resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return getResource().toString()+'@'+getNode().toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ResourceNode)) return false;
        ResourceNode other = (ResourceNode)o;
        return
            resource.equals(other.resource)
            && node.equals(other.node)
        ;
    }

    @Override
    public int hashCode() {
        return resource.hashCode() * 31 + node.hashCode();
    }

    /**
     * Gets the resource this represents.
     */
    public R getResource() {
        return resource;
    }

    /**
     * Gets the node this represents.
     */
    public Node getNode() {
        return node;
    }

    /**
     * Gets the set of node DNS records that must all the the same and
     * match the resource's masterRecords for this node to be considered
     * a master.
     */
    public Set<Name> getNodeRecords() {
        return nodeRecords;
    }

    /**
     * Gets the current status of this resource node.
     */
    public NodeDnsStatus getNodeStatus() {
        NodeDnsStatus status = NodeDnsStatus.UNKNOWN;
        status = AppCluster.max(status, resource.getDnsMonitor().getLastResult().getNodeResults().get(getNode()).getNodeStatus());
        return status;
    }
}
