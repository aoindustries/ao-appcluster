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

import java.util.Collection;
import java.util.SortedSet;
import org.xbill.DNS.Name;

/**
 * Contains the results of one DNS record lookup.
 *
 * @author  AO Industries, Inc.
 */
public class DnsLookupResult {

    private final Name name;
    private final DnsLookupStatus status;
    private final SortedSet<String> addresses;
    private final SortedSet<String> statusMessages;

    /**
     * Sorts the addresses as they are added.
     */
    DnsLookupResult(
        Name name,
        DnsLookupStatus status,
        String[] addresses,
        Collection<String> statusMessages
    ) {
        this.name = name;
        this.status = status;
        this.addresses = ResourceDnsResult.getUnmodifiableSortedSet(addresses, null); // Sorts lexically for speed since not human readable
        assert status==DnsLookupStatus.SUCCESSFUL || status==DnsLookupStatus.WARNING ? !this.addresses.isEmpty() : this.addresses.isEmpty();
        this.statusMessages = ResourceDnsResult.getUnmodifiableSortedSet(statusMessages, ResourceDnsResult.defaultLocaleCollator);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof DnsLookupResult)) return false;
        DnsLookupResult other = (DnsLookupResult)o;
        return
            name.equals(other.name)
            && status==other.status
            && addresses.equals(other.addresses)
            && statusMessages.equals(other.statusMessages)
        ;
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 31 + status.hashCode();
    }

    public Name getName() {
        return name;
    }

    public DnsLookupStatus getStatus() {
        return status;
    }

    /**
     * Only relevant for SUCCESSFUL lookups.
     */
    public SortedSet<String> getAddresses() {
        return addresses;
    }

    /**
     * Gets the status messages for this lookup.
     */
    public SortedSet<String> getStatusMessages() {
        return statusMessages;
    }
}
