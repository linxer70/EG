/*
 * Decompiled with CFR 0.152.
 */
package egframe.common.SysChat;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Deprecated(since="6.3", forRemoval=true)
public interface LicenseStorage {
    public List<String> getUserEntries(String var1, YearMonth var2);

    public void addUserEntry(String var1, YearMonth var2, String var3);

    public Map<String, LocalDate> getLatestLicenseEvents(String var1);

    public void setLicenseEvent(String var1, String var2, LocalDate var3);
}
