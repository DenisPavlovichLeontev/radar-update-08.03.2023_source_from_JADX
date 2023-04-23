package p005ar.com.hjg.pngj.chunks;

import kotlin.UByte;
import p005ar.com.hjg.pngj.ImageInfo;
import p005ar.com.hjg.pngj.PngjException;
import p005ar.com.hjg.pngj.chunks.PngChunk;

/* renamed from: ar.com.hjg.pngj.chunks.PngChunkICCP */
public class PngChunkICCP extends PngChunkSingle {

    /* renamed from: ID */
    public static final String f128ID = "iCCP";
    private byte[] compressedProfile;
    private String profileName;

    public PngChunkICCP(ImageInfo imageInfo) {
        super("iCCP", imageInfo);
    }

    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_PLTE_AND_IDAT;
    }

    public ChunkRaw createRawChunk() {
        ChunkRaw createEmptyChunk = createEmptyChunk(this.profileName.length() + this.compressedProfile.length + 2, true);
        System.arraycopy(ChunkHelper.toBytes(this.profileName), 0, createEmptyChunk.data, 0, this.profileName.length());
        createEmptyChunk.data[this.profileName.length()] = 0;
        createEmptyChunk.data[this.profileName.length() + 1] = 0;
        System.arraycopy(this.compressedProfile, 0, createEmptyChunk.data, this.profileName.length() + 2, this.compressedProfile.length);
        return createEmptyChunk;
    }

    public void parseFromRaw(ChunkRaw chunkRaw) {
        int posNullByte = ChunkHelper.posNullByte(chunkRaw.data);
        this.profileName = ChunkHelper.toString(chunkRaw.data, 0, posNullByte);
        if ((chunkRaw.data[posNullByte + 1] & UByte.MAX_VALUE) == 0) {
            int i = posNullByte + 2;
            int length = chunkRaw.data.length - i;
            this.compressedProfile = new byte[length];
            System.arraycopy(chunkRaw.data, i, this.compressedProfile, 0, length);
            return;
        }
        throw new PngjException("bad compression for ChunkTypeICCP");
    }

    public void setProfileNameAndContent(String str, byte[] bArr) {
        this.profileName = str;
        this.compressedProfile = ChunkHelper.compressBytes(bArr, true);
    }

    public void setProfileNameAndContent(String str, String str2) {
        setProfileNameAndContent(str, ChunkHelper.toBytes(str2));
    }

    public String getProfileName() {
        return this.profileName;
    }

    public byte[] getProfile() {
        return ChunkHelper.compressBytes(this.compressedProfile, false);
    }

    public String getProfileAsString() {
        return ChunkHelper.toString(getProfile());
    }
}
