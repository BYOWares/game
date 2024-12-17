/*
 * Copyright BYOWares
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.byowares.game.utils.enums;

/**
 * Represents Size in binary prefix (power of 2, not 10).
 *
 * @since XXX
 */
public enum Size {

    /** The lowest granularity, the byte. */
    BYTE(Size.BYTE_SCALE),
    /** 1 KibiByte = 1024 Bytes. */
    KB(Size.KIBI_SCALE),
    /** 1 MebiByte = 1024 KibiBytes. */
    MB(Size.MEBI_SCALE),
    /** 1 GibiByte = 1024 MebiBytes. */
    GB(Size.GIBI_SCALE),
    /** 1 TebiByte = 1024 GibiBytes. */
    TB(Size.TEBI_SCALE);

    private static final long BINARY_PREFIX = 1024L;
    private static final long BYTE_SCALE = 1L;
    private static final long KIBI_SCALE = BINARY_PREFIX * BYTE_SCALE;
    private static final long MEBI_SCALE = BINARY_PREFIX * KIBI_SCALE;
    private static final long GIBI_SCALE = BINARY_PREFIX * MEBI_SCALE;
    private static final long TEBI_SCALE = BINARY_PREFIX * GIBI_SCALE;

    private final long scale;
    private final long maxBytes;
    private final long ratioKibis;
    private final long maxKibis;
    private final long ratioMebis;
    private final long maxMebis;
    private final long ratioGibis;
    private final long maxGibis;
    private final long ratioTebis;
    private final long maxTebis;

    Size(final long s) {
        this.scale = s;

        this.maxBytes = Long.MAX_VALUE / s;
        this.ratioKibis = ratio(s, KIBI_SCALE);
        this.ratioMebis = ratio(s, MEBI_SCALE);
        this.ratioGibis = ratio(s, GIBI_SCALE);
        this.ratioTebis = ratio(s, TEBI_SCALE);

        this.maxKibis = Long.MAX_VALUE / this.ratioKibis;
        this.maxMebis = Long.MAX_VALUE / this.ratioMebis;
        this.maxGibis = Long.MAX_VALUE / this.ratioGibis;
        this.maxTebis = Long.MAX_VALUE / this.ratioTebis;
    }

    private static long ratio(
            final long currentScale,
            final long targetScale
    ) {
        return (currentScale >= targetScale) ? (currentScale / targetScale) : (targetScale / currentScale);
    }

    /**
     * Convert a size in the given unit to Bytes. Conversions from finer to coarser units truncate number, thus losing
     * precision. For example, converting {@code 1023} Bytes to KibiBytes results in {@code 0}. Conversions from coarser
     * to finer units with arguments that would numerically overflow saturate to {@link java.lang.Long#MIN_VALUE Long
     * #MIN_VALUE} if negative or {@link java.lang.Long#MAX_VALUE Long#MAX_VALUE} if positive.
     *
     * @param size The size to convert to Bytes.
     *
     * @return The converted size in Bytes,
     * or {@link java.lang.Long#MIN_VALUE Long#MIN_VALUE} if conversion would negatively overflow,
     * or {@link java.lang.Long#MAX_VALUE Long#MAX_VALUE} if it would positively overflow.
     */
    public long toBytes(final long size) {
        return convert(size, this.scale, BYTE_SCALE, this.maxBytes, this.scale);
    }

    private static long convert(
            final long size,
            final long currentScale,
            final long targetScale,
            final long maxSize,
            final long ratio
    ) {
        if (currentScale <= targetScale) return (currentScale == targetScale) ? size : size / ratio;
        else if (size > maxSize) return Long.MAX_VALUE;
        else if (size < -maxSize) return Long.MIN_VALUE;
        else return size * ratio;
    }

    /**
     * Convert a size in the given unit to KibiBytes. Conversions from finer to coarser units truncate number, thus
     * losing precision. For example, converting {@code 1023} Bytes to KibiBytes results in {@code 0}. Conversions from
     * coarser to finer units with arguments that would numerically overflow saturate to
     * {@link java.lang.Long#MIN_VALUE Long#MIN_VALUE} if negative or {@link java.lang.Long#MAX_VALUE Long#MAX_VALUE}
     * if positive.
     *
     * @param size The size to convert to KibiBytes.
     *
     * @return The converted size in KibiBytes,
     * or {@link java.lang.Long#MIN_VALUE Long#MIN_VALUE} if conversion would negatively overflow,
     * or {@link java.lang.Long#MAX_VALUE Long#MAX_VALUE} if it would positively overflow.
     */
    public long toKibiBytes(final long size) {
        return convert(size, this.scale, KIBI_SCALE, this.maxKibis, this.ratioKibis);
    }

    /**
     * Convert a size in the given unit to MebiBytes. Conversions from finer to coarser units truncate number, thus
     * losing precision. For example, converting {@code 1023} Bytes to KibiBytes results in {@code 0}. Conversions from
     * coarser to finer units with arguments that would numerically overflow saturate to
     * {@link java.lang.Long#MIN_VALUE Long#MIN_VALUE} if negative or {@link java.lang.Long#MAX_VALUE Long#MAX_VALUE}
     * if positive.
     *
     * @param size The size to convert to MebiBytes.
     *
     * @return The converted size in MebiBytes,
     * or {@link java.lang.Long#MIN_VALUE Long#MIN_VALUE} if conversion would negatively overflow,
     * or {@link java.lang.Long#MAX_VALUE Long#MAX_VALUE} if it would positively overflow.
     */
    public long toMebiBytes(final long size) {
        return convert(size, this.scale, MEBI_SCALE, this.maxMebis, this.ratioMebis);
    }

    /**
     * Convert a size in the given unit to GibiBytes. Conversions from finer to coarser units truncate number, thus
     * losing precision. For example, converting {@code 1023} Bytes to KibiBytes results in {@code 0}. Conversions from
     * coarser to finer units with arguments that would numerically overflow saturate to
     * {@link java.lang.Long#MIN_VALUE Long#MIN_VALUE} if negative or {@link java.lang.Long#MAX_VALUE Long#MAX_VALUE}
     * if positive.
     *
     * @param size The size to convert to GibiBytes.
     *
     * @return The converted size in GibiBytes,
     * or {@link java.lang.Long#MIN_VALUE Long#MIN_VALUE} if conversion would negatively overflow,
     * or {@link java.lang.Long#MAX_VALUE Long#MAX_VALUE} if it would positively overflow.
     */
    public long toGibiBytes(final long size) {
        return convert(size, this.scale, GIBI_SCALE, this.maxGibis, this.ratioGibis);
    }

    /**
     * Convert a size in the given unit to TebiBytes. Conversions from finer to coarser units truncate number, thus
     * losing precision. For example, converting {@code 1023} Bytes to KibiBytes results in {@code 0}. Conversions from
     * coarser to finer units with arguments that would numerically overflow saturate to
     * {@link java.lang.Long#MIN_VALUE Long#MIN_VALUE} if negative or {@link java.lang.Long#MAX_VALUE Long#MAX_VALUE}
     * if positive.
     *
     * @param size The size to convert to TebiBytes.
     *
     * @return The converted size in TebiBytes,
     * or {@link java.lang.Long#MIN_VALUE Long#MIN_VALUE} if conversion would negatively overflow,
     * or {@link java.lang.Long#MAX_VALUE Long#MAX_VALUE} if it would positively overflow.
     */
    public long toTebiBytes(final long size) {
        return convert(size, this.scale, TEBI_SCALE, this.maxTebis, this.ratioTebis);
    }
}
