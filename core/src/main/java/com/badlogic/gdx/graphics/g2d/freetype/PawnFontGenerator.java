package com.badlogic.gdx.graphics.g2d.freetype;

import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Null;

import java.nio.ByteBuffer;

public class PawnFontGenerator extends FreeTypeFontGenerator {
    public PawnFontGenerator(FileHandle fontFile) {
        super(fontFile);
    }

    private int getLoadingFlags (@NonNull FreeTypeFontParameter parameter) {
        int loadingFlags = FreeType.FT_LOAD_DEFAULT;
        switch (parameter.hinting) {
            case None:
                loadingFlags |= FreeType.FT_LOAD_NO_HINTING;
                break;
            case Slight:
                loadingFlags |= FreeType.FT_LOAD_TARGET_LIGHT;
                break;
            case Medium:
                loadingFlags |= FreeType.FT_LOAD_TARGET_NORMAL;
                break;
            case Full:
                loadingFlags |= FreeType.FT_LOAD_TARGET_MONO;
                break;
            case AutoSlight:
                loadingFlags |= FreeType.FT_LOAD_FORCE_AUTOHINT | FreeType.FT_LOAD_TARGET_LIGHT;
                break;
            case AutoMedium:
                loadingFlags |= FreeType.FT_LOAD_FORCE_AUTOHINT | FreeType.FT_LOAD_TARGET_NORMAL;
                break;
            case AutoFull:
                loadingFlags |= FreeType.FT_LOAD_FORCE_AUTOHINT | FreeType.FT_LOAD_TARGET_MONO;
                break;
        }
        return loadingFlags;
    }

    private boolean loadChar (int c, int flags) {
        return face.loadChar(c, flags);
    }

    @Override
    protected @Null
    BitmapFont.Glyph createGlyph (char c, @NonNull FreeTypeBitmapFontData data, @NonNull FreeTypeFontParameter parameter, FreeType.Stroker stroker,
                                  float baseLine, PixmapPacker packer) {

        data.setLineHeight(parameter.size + (parameter.borderWidth * 2));
        boolean missing = face.getCharIndex(c) == 0 && c != 0;
        if (missing) return null;

        if (!loadChar(c, getLoadingFlags(parameter))) return null;

        FreeType.GlyphSlot slot = face.getGlyph();
        FreeType.Glyph mainGlyph = slot.getGlyph();
        try {
            mainGlyph.toBitmap(parameter.mono ? FreeType.FT_RENDER_MODE_MONO : FreeType.FT_RENDER_MODE_NORMAL);
        } catch (GdxRuntimeException e) {
            mainGlyph.dispose();
            Gdx.app.log("FreeTypeFontGenerator", "Couldn't render char: " + c);
            return null;
        }
        FreeType.Bitmap mainBitmap = mainGlyph.getBitmap();
        Pixmap mainPixmap = mainBitmap.getPixmap(Pixmap.Format.RGBA8888, parameter.color, parameter.gamma);

        if (mainBitmap.getWidth() != 0 && mainBitmap.getRows() != 0) {
            if (parameter.borderWidth > 0) {
                // execute stroker; this generates a glyph "extended" along the outline
                int top = mainGlyph.getTop(), left = mainGlyph.getLeft();
                FreeType.Glyph borderGlyph = slot.getGlyph();
                borderGlyph.strokeBorder(stroker, false);
                borderGlyph.toBitmap(parameter.mono ? FreeType.FT_RENDER_MODE_MONO : FreeType.FT_RENDER_MODE_NORMAL);
                int offsetX = left - borderGlyph.getLeft();
                int offsetY = -(top - borderGlyph.getTop());

                // Render border (pixmap is bigger than main).
                FreeType.Bitmap borderBitmap = borderGlyph.getBitmap();
                Pixmap borderPixmap = borderBitmap.getPixmap(Pixmap.Format.RGBA8888, parameter.borderColor, parameter.borderGamma);

                // Draw main glyph on top of border.
                for (int i = 0, n = parameter.renderCount; i < n; i++)
                    borderPixmap.drawPixmap(mainPixmap, offsetX, offsetY);

                Pixmap fixedPixmap = new Pixmap(borderPixmap.getWidth(), borderPixmap.getHeight() + (int) (parameter.borderWidth * 2), Pixmap.Format.RGBA8888);
                fixedPixmap.drawPixmap(borderPixmap, 0, (int) parameter.borderWidth);

                mainPixmap.dispose();
                mainGlyph.dispose();
                mainPixmap = fixedPixmap;
                mainGlyph = borderGlyph;
            }

            if (parameter.shadowOffsetX != 0 || parameter.shadowOffsetY != 0) {
                int mainW = mainPixmap.getWidth(), mainH = mainPixmap.getHeight();
                int shadowOffsetX = Math.max(parameter.shadowOffsetX, 0), shadowOffsetY = Math.max(parameter.shadowOffsetY, 0);
                int shadowW = mainW + Math.abs(parameter.shadowOffsetX), shadowH = mainH + Math.abs(parameter.shadowOffsetY);
                Pixmap shadowPixmap = new Pixmap(shadowW, shadowH, mainPixmap.getFormat());

                Color shadowColor = parameter.shadowColor;
                float a = shadowColor.a;
                if (a != 0) {
                    byte r = (byte)(shadowColor.r * 255), g = (byte)(shadowColor.g * 255), b = (byte)(shadowColor.b * 255);
                    ByteBuffer mainPixels = mainPixmap.getPixels();
                    ByteBuffer shadowPixels = shadowPixmap.getPixels();
                    for (int y = 0; y < mainH; y++) {
                        int shadowRow = shadowW * (y + shadowOffsetY) + shadowOffsetX;
                        for (int x = 0; x < mainW; x++) {
                            int mainPixel = (mainW * y + x) * 4;
                            byte mainA = mainPixels.get(mainPixel + 3);
                            if (mainA == 0) continue;
                            int shadowPixel = (shadowRow + x) * 4;
                            shadowPixels.put(shadowPixel, r);
                            shadowPixels.put(shadowPixel + 1, g);
                            shadowPixels.put(shadowPixel + 2, b);
                            shadowPixels.put(shadowPixel + 3, (byte)((mainA & 0xff) * a));
                        }
                    }
                }

                // Draw main glyph (with any border) on top of shadow.
                for (int i = 0, n = parameter.renderCount; i < n; i++)
                    shadowPixmap.drawPixmap(mainPixmap, Math.max(-parameter.shadowOffsetX, 0), Math.max(-parameter.shadowOffsetY, 0));
                mainPixmap.dispose();
                mainPixmap = shadowPixmap;
            } else if (parameter.borderWidth == 0) {
                // No shadow and no border, draw glyph additional times.
                for (int i = 0, n = parameter.renderCount - 1; i < n; i++)
                    mainPixmap.drawPixmap(mainPixmap, 0, 0);
            }

            if (parameter.padTop > 0 || parameter.padLeft > 0 || parameter.padBottom > 0 || parameter.padRight > 0) {
                Pixmap padPixmap = new Pixmap(mainPixmap.getWidth() + parameter.padLeft + parameter.padRight,
                        mainPixmap.getHeight() + parameter.padTop + parameter.padBottom, mainPixmap.getFormat());
                padPixmap.setBlending(Pixmap.Blending.None);
                padPixmap.drawPixmap(mainPixmap, parameter.padLeft, parameter.padTop);
                mainPixmap.dispose();
                mainPixmap = padPixmap;
            }
        }

        FreeType.GlyphMetrics metrics = slot.getMetrics();
        BitmapFont.Glyph glyph = new BitmapFont.Glyph();
        glyph.id = c;
        glyph.width = mainPixmap.getWidth();
        glyph.height = mainPixmap.getHeight();
        glyph.xoffset = mainGlyph.getLeft();
        if (parameter.flip)
            glyph.yoffset = -mainGlyph.getTop() + (int)baseLine;
        else
            glyph.yoffset = -(glyph.height - mainGlyph.getTop()) - (int)baseLine;
        glyph.xadvance = FreeType.toInt(metrics.getHoriAdvance()) + (int)parameter.borderWidth + parameter.spaceX;

        if (bitmapped) {
            mainPixmap.setColor(Color.CLEAR);
            mainPixmap.fill();
            ByteBuffer buf = mainBitmap.getBuffer();
            int whiteIntBits = Color.WHITE.toIntBits();
            int clearIntBits = Color.CLEAR.toIntBits();
            for (int h = 0; h < glyph.height; h++) {
                int idx = h * mainBitmap.getPitch();
                for (int w = 0; w < (glyph.width + glyph.xoffset); w++) {
                    int bit = (buf.get(idx + (w / 8)) >>> (7 - (w % 8))) & 1;
                    mainPixmap.drawPixel(w, h, ((bit == 1) ? whiteIntBits : clearIntBits));
                }
            }
        }

        Rectangle rect = packer.pack(mainPixmap);
        glyph.page = packer.getPages().size - 1; // Glyph is always packed into the last page for now.
        glyph.srcX = (int)rect.x;
        glyph.srcY = (int)rect.y;

        // If a page was added, create a new texture region for the incrementally added glyph.
        if (parameter.incremental && data.regions != null && data.regions.size <= glyph.page)
            packer.updateTextureRegions(data.regions, parameter.minFilter, parameter.magFilter, parameter.genMipMaps);

        mainPixmap.dispose();
        mainGlyph.dispose();

        return glyph;
    }
}
