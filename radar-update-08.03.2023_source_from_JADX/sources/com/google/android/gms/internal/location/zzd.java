package com.google.android.gms.internal.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.Arrays;
import java.util.List;

/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
public final class zzd extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzd> CREATOR = new zze();
    private final int zza;
    private final int zzb;
    private final String zzc;
    private final String zzd;
    private final int zze;
    private final String zzf;
    private final zzd zzg;
    private final List zzh;

    static {
        Process.myUid();
        Process.myPid();
    }

    zzd(int i, int i2, String str, String str2, String str3, int i3, List list, zzd zzd2) {
        this.zza = i;
        this.zzb = i2;
        this.zzc = str;
        this.zzd = str2;
        this.zzf = str3;
        this.zze = i3;
        this.zzh = zzds.zzj(list);
        this.zzg = zzd2;
    }

    public final boolean equals(Object obj) {
        if (obj instanceof zzd) {
            zzd zzd2 = (zzd) obj;
            if (this.zza != zzd2.zza || this.zzb != zzd2.zzb || this.zze != zzd2.zze || !this.zzc.equals(zzd2.zzc) || !zzdl.zza(this.zzd, zzd2.zzd) || !zzdl.zza(this.zzf, zzd2.zzf) || !zzdl.zza(this.zzg, zzd2.zzg) || !this.zzh.equals(zzd2.zzh)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.zza), this.zzc, this.zzd, this.zzf});
    }

    public final String toString() {
        int length = this.zzc.length() + 18;
        String str = this.zzd;
        if (str != null) {
            length += str.length();
        }
        StringBuilder sb = new StringBuilder(length);
        sb.append(this.zza);
        sb.append("/");
        sb.append(this.zzc);
        if (this.zzd != null) {
            sb.append("[");
            if (this.zzd.startsWith(this.zzc)) {
                sb.append(this.zzd, this.zzc.length(), this.zzd.length());
            } else {
                sb.append(this.zzd);
            }
            sb.append("]");
        }
        if (this.zzf != null) {
            sb.append("/");
            sb.append(Integer.toHexString(this.zzf.hashCode()));
        }
        return sb.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zza);
        SafeParcelWriter.writeInt(parcel, 2, this.zzb);
        SafeParcelWriter.writeString(parcel, 3, this.zzc, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzd, false);
        SafeParcelWriter.writeInt(parcel, 5, this.zze);
        SafeParcelWriter.writeString(parcel, 6, this.zzf, false);
        SafeParcelWriter.writeParcelable(parcel, 7, this.zzg, i, false);
        SafeParcelWriter.writeTypedList(parcel, 8, this.zzh, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
