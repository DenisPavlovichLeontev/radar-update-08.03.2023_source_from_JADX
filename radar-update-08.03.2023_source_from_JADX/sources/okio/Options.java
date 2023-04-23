package okio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo19893d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\u0018\u0000 \u00152\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004:\u0001\u0015B\u001f\b\u0002\u0012\u000e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u0011\u0010\u0013\u001a\u00020\u00022\u0006\u0010\u0014\u001a\u00020\u000eH\u0002R\u001e\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u0006X\u0004¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\r\u001a\u00020\u000e8VX\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0014\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u0016"}, mo19894d2 = {"Lokio/Options;", "Lkotlin/collections/AbstractList;", "Lokio/ByteString;", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "byteStrings", "", "trie", "", "([Lokio/ByteString;[I)V", "getByteStrings$okio", "()[Lokio/ByteString;", "[Lokio/ByteString;", "size", "", "getSize", "()I", "getTrie$okio", "()[I", "get", "index", "Companion", "okio"}, mo19895k = 1, mo19896mv = {1, 5, 1}, mo19898xi = 48)
/* compiled from: Options.kt */
public final class Options extends AbstractList<ByteString> implements RandomAccess {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final ByteString[] byteStrings;
    private final int[] trie;

    public /* synthetic */ Options(ByteString[] byteStringArr, int[] iArr, DefaultConstructorMarker defaultConstructorMarker) {
        this(byteStringArr, iArr);
    }

    @JvmStatic
    /* renamed from: of */
    public static final Options m144of(ByteString... byteStringArr) {
        return Companion.mo26034of(byteStringArr);
    }

    public final /* bridge */ boolean contains(Object obj) {
        if (!(obj instanceof ByteString)) {
            return false;
        }
        return contains((ByteString) obj);
    }

    public /* bridge */ boolean contains(ByteString byteString) {
        return super.contains(byteString);
    }

    public final /* bridge */ int indexOf(Object obj) {
        if (!(obj instanceof ByteString)) {
            return -1;
        }
        return indexOf((ByteString) obj);
    }

    public /* bridge */ int indexOf(ByteString byteString) {
        return super.indexOf(byteString);
    }

    public final /* bridge */ int lastIndexOf(Object obj) {
        if (!(obj instanceof ByteString)) {
            return -1;
        }
        return lastIndexOf((ByteString) obj);
    }

    public /* bridge */ int lastIndexOf(ByteString byteString) {
        return super.lastIndexOf(byteString);
    }

    public final ByteString[] getByteStrings$okio() {
        return this.byteStrings;
    }

    public final int[] getTrie$okio() {
        return this.trie;
    }

    private Options(ByteString[] byteStringArr, int[] iArr) {
        this.byteStrings = byteStringArr;
        this.trie = iArr;
    }

    public int getSize() {
        return this.byteStrings.length;
    }

    public ByteString get(int i) {
        return this.byteStrings[i];
    }

    @Metadata(mo19893d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002JT\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\r2\b\b\u0002\u0010\u0012\u001a\u00020\r2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH\u0002J!\u0010\u0014\u001a\u00020\u00152\u0012\u0010\u000e\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00100\u0016\"\u00020\u0010H\u0007¢\u0006\u0002\u0010\u0017R\u0018\u0010\u0003\u001a\u00020\u0004*\u00020\u00058BX\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0018"}, mo19894d2 = {"Lokio/Options$Companion;", "", "()V", "intCount", "", "Lokio/Buffer;", "getIntCount", "(Lokio/Buffer;)J", "buildTrieRecursive", "", "nodeOffset", "node", "byteStringOffset", "", "byteStrings", "", "Lokio/ByteString;", "fromIndex", "toIndex", "indexes", "of", "Lokio/Options;", "", "([Lokio/ByteString;)Lokio/Options;", "okio"}, mo19895k = 1, mo19896mv = {1, 5, 1}, mo19898xi = 48)
    /* compiled from: Options.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        /* renamed from: of */
        public final Options mo26034of(ByteString... byteStringArr) {
            ByteString[] byteStringArr2 = byteStringArr;
            Intrinsics.checkNotNullParameter(byteStringArr2, "byteStrings");
            int i = 0;
            if (byteStringArr2.length == 0) {
                return new Options(new ByteString[0], new int[]{0, -1}, (DefaultConstructorMarker) null);
            }
            List mutableList = ArraysKt.toMutableList((T[]) byteStringArr);
            CollectionsKt.sort(mutableList);
            Collection arrayList = new ArrayList(byteStringArr2.length);
            for (ByteString byteString : byteStringArr2) {
                arrayList.add(-1);
            }
            Object[] array = ((List) arrayList).toArray(new Integer[0]);
            if (array != null) {
                Integer[] numArr = (Integer[]) array;
                List mutableListOf = CollectionsKt.mutableListOf(Arrays.copyOf(numArr, numArr.length));
                int length = byteStringArr2.length;
                int i2 = 0;
                int i3 = 0;
                while (i2 < length) {
                    mutableListOf.set(CollectionsKt.binarySearch$default(mutableList, (Comparable) byteStringArr2[i2], 0, 0, 6, (Object) null), Integer.valueOf(i3));
                    i2++;
                    i3++;
                }
                if (((ByteString) mutableList.get(0)).size() > 0) {
                    int i4 = 0;
                    while (i4 < mutableList.size()) {
                        ByteString byteString2 = (ByteString) mutableList.get(i4);
                        int i5 = i4 + 1;
                        int i6 = i5;
                        while (i6 < mutableList.size()) {
                            ByteString byteString3 = (ByteString) mutableList.get(i6);
                            if (!byteString3.startsWith(byteString2)) {
                                continue;
                                break;
                            }
                            if (!(byteString3.size() != byteString2.size())) {
                                throw new IllegalArgumentException(Intrinsics.stringPlus("duplicate option: ", byteString3).toString());
                            } else if (((Number) mutableListOf.get(i6)).intValue() > ((Number) mutableListOf.get(i4)).intValue()) {
                                mutableList.remove(i6);
                                mutableListOf.remove(i6);
                            } else {
                                i6++;
                            }
                        }
                        i4 = i5;
                    }
                    Buffer buffer = new Buffer();
                    buildTrieRecursive$default(this, 0, buffer, 0, mutableList, 0, 0, mutableListOf, 53, (Object) null);
                    int[] iArr = new int[((int) getIntCount(buffer))];
                    while (!buffer.exhausted()) {
                        iArr[i] = buffer.readInt();
                        i++;
                    }
                    Object[] copyOf = Arrays.copyOf(byteStringArr2, byteStringArr2.length);
                    Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, size)");
                    return new Options((ByteString[]) copyOf, iArr, (DefaultConstructorMarker) null);
                }
                throw new IllegalArgumentException("the empty byte string is not a supported option".toString());
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }

        static /* synthetic */ void buildTrieRecursive$default(Companion companion, long j, Buffer buffer, int i, List list, int i2, int i3, List list2, int i4, Object obj) {
            int i5;
            long j2 = (i4 & 1) != 0 ? 0 : j;
            int i6 = (i4 & 4) != 0 ? 0 : i;
            int i7 = (i4 & 16) != 0 ? 0 : i2;
            if ((i4 & 32) != 0) {
                i5 = list.size();
            } else {
                i5 = i3;
            }
            companion.buildTrieRecursive(j2, buffer, i6, list, i7, i5, list2);
        }

        private final void buildTrieRecursive(long j, Buffer buffer, int i, List<? extends ByteString> list, int i2, int i3, List<Integer> list2) {
            int i4;
            int i5;
            int i6;
            int i7;
            Buffer buffer2;
            int i8;
            Buffer buffer3 = buffer;
            int i9 = i;
            List<? extends ByteString> list3 = list;
            int i10 = i2;
            int i11 = i3;
            List<Integer> list4 = list2;
            if (i10 < i11) {
                if (i10 < i11) {
                    int i12 = i10;
                    while (true) {
                        int i13 = i12 + 1;
                        if (!(((ByteString) list3.get(i12)).size() >= i9)) {
                            throw new IllegalArgumentException("Failed requirement.".toString());
                        } else if (i13 >= i11) {
                            break;
                        } else {
                            i12 = i13;
                        }
                    }
                }
                ByteString byteString = (ByteString) list.get(i2);
                ByteString byteString2 = (ByteString) list3.get(i11 - 1);
                if (i9 == byteString.size()) {
                    int intValue = list4.get(i10).intValue();
                    int i14 = i10 + 1;
                    i4 = i14;
                    i5 = intValue;
                    byteString = (ByteString) list3.get(i14);
                } else {
                    i4 = i10;
                    i5 = -1;
                }
                if (byteString.getByte(i9) != byteString2.getByte(i9)) {
                    int i15 = i4 + 1;
                    int i16 = 1;
                    if (i15 < i11) {
                        while (true) {
                            int i17 = i15 + 1;
                            if (((ByteString) list3.get(i15 - 1)).getByte(i9) != ((ByteString) list3.get(i15)).getByte(i9)) {
                                i16++;
                            }
                            if (i17 >= i11) {
                                break;
                            }
                            i15 = i17;
                        }
                    }
                    long intCount = j + getIntCount(buffer3) + ((long) 2) + ((long) (i16 * 2));
                    buffer3.writeInt(i16);
                    buffer3.writeInt(i5);
                    if (i4 < i11) {
                        int i18 = i4;
                        while (true) {
                            int i19 = i18 + 1;
                            byte b = ((ByteString) list3.get(i18)).getByte(i9);
                            if (i18 == i4 || b != ((ByteString) list3.get(i18 - 1)).getByte(i9)) {
                                buffer3.writeInt((int) b & UByte.MAX_VALUE);
                            }
                            if (i19 >= i11) {
                                break;
                            }
                            i18 = i19;
                        }
                    }
                    Buffer buffer4 = new Buffer();
                    while (i4 < i11) {
                        byte b2 = ((ByteString) list3.get(i4)).getByte(i9);
                        int i20 = i4 + 1;
                        if (i20 < i11) {
                            int i21 = i20;
                            while (true) {
                                int i22 = i21 + 1;
                                if (b2 != ((ByteString) list3.get(i21)).getByte(i9)) {
                                    i7 = i21;
                                    break;
                                } else if (i22 >= i11) {
                                    break;
                                } else {
                                    i21 = i22;
                                }
                            }
                        }
                        i7 = i11;
                        if (i20 == i7 && i9 + 1 == ((ByteString) list3.get(i4)).size()) {
                            buffer3.writeInt(list4.get(i4).intValue());
                            i8 = i7;
                            buffer2 = buffer4;
                        } else {
                            buffer3.writeInt(((int) (intCount + getIntCount(buffer4))) * -1);
                            i8 = i7;
                            buffer2 = buffer4;
                            buildTrieRecursive(intCount, buffer4, i9 + 1, list, i4, i7, list2);
                        }
                        i4 = i8;
                        buffer4 = buffer2;
                    }
                    buffer3.writeAll(buffer4);
                    return;
                }
                int min = Math.min(byteString.size(), byteString2.size());
                if (i9 < min) {
                    int i23 = i9;
                    int i24 = 0;
                    while (true) {
                        int i25 = i23 + 1;
                        if (byteString.getByte(i23) != byteString2.getByte(i23)) {
                            break;
                        }
                        i24++;
                        if (i25 >= min) {
                            break;
                        }
                        i23 = i25;
                    }
                    i6 = i24;
                } else {
                    i6 = 0;
                }
                long intCount2 = j + getIntCount(buffer3) + ((long) 2) + ((long) i6) + 1;
                buffer3.writeInt(-i6);
                buffer3.writeInt(i5);
                int i26 = i9 + i6;
                if (i9 < i26) {
                    while (true) {
                        int i27 = i9 + 1;
                        buffer3.writeInt((int) byteString.getByte(i9) & UByte.MAX_VALUE);
                        if (i27 >= i26) {
                            break;
                        }
                        i9 = i27;
                    }
                }
                if (i4 + 1 == i11) {
                    if (i26 == ((ByteString) list3.get(i4)).size()) {
                        buffer3.writeInt(list4.get(i4).intValue());
                        return;
                    }
                    throw new IllegalStateException("Check failed.".toString());
                }
                Buffer buffer5 = new Buffer();
                buffer3.writeInt(((int) (getIntCount(buffer5) + intCount2)) * -1);
                buildTrieRecursive(intCount2, buffer5, i26, list, i4, i3, list2);
                buffer3.writeAll(buffer5);
                return;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }

        private final long getIntCount(Buffer buffer) {
            return buffer.size() / ((long) 4);
        }
    }
}
