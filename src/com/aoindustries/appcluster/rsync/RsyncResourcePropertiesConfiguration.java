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
package com.aoindustries.appcluster.rsync;

import com.aoindustries.appcluster.AppCluster;
import com.aoindustries.appcluster.AppClusterConfigurationException;
import com.aoindustries.appcluster.AppClusterPropertiesConfiguration;
import com.aoindustries.appcluster.ResourceNode;
import com.aoindustries.appcluster.ResourcePropertiesConfiguration;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The configuration for a rsync resource.
 *
 * @author  AO Industries, Inc.
 */
public class RsyncResourcePropertiesConfiguration extends ResourcePropertiesConfiguration<RsyncResource,RsyncResourceNode> implements RsyncResourceConfiguration {

    private final boolean allowMultiMaster;
    private final boolean delete;

    protected RsyncResourcePropertiesConfiguration(AppClusterPropertiesConfiguration properties, String id) throws AppClusterConfigurationException {
        super(properties, id);
        this.allowMultiMaster = properties.getBoolean("appcluster.resource."+id+"."+type+".allowMultiMaster");
        this.delete = properties.getBoolean("appcluster.resource."+id+"."+type+".delete");
    }

    @Override
    public boolean getAllowMultiMaster() {
        return allowMultiMaster;
    }

    @Override
    public boolean isDelete() {
        return delete;
    }

    @Override
    public Set<? extends RsyncResourceNodePropertiesConfiguration> getResourceNodeConfigurations() throws AppClusterConfigurationException {
        String resourceId = getId();
        Set<String> nodeIds = properties.getUniqueStrings("appcluster.resource."+id+".nodes");
        Set<RsyncResourceNodePropertiesConfiguration> resourceNodes = new LinkedHashSet<RsyncResourceNodePropertiesConfiguration>(nodeIds.size()*4/3+1);
        for(String nodeId : nodeIds) {
            if(!resourceNodes.add(new RsyncResourceNodePropertiesConfiguration(properties, resourceId, nodeId, type))) throw new AssertionError();
        }
        return Collections.unmodifiableSet(resourceNodes);
    }

    @Override
    public RsyncResource newResource(AppCluster cluster, Collection<? extends ResourceNode<?,?>> resourceNodes) throws AppClusterConfigurationException {
        return new RsyncResource(cluster, this, resourceNodes);
    }
}
