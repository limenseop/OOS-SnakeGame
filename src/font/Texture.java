/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */


package src.font;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

public class Texture {
	
	private int id;
	
	private int width;
	private int height;
	
	public void setParameter(int name, int value) {
    	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, name, value);
    }
	
	public void bind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
	}
	
	public void unbind(){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	
	public int getId() {
		return id;
	}

	public Texture(String path){
		// create buffers to store data.
		ByteBuffer image;
		IntBuffer w = MemoryUtil.memAllocInt(1);
		IntBuffer h = MemoryUtil.memAllocInt(1);
		IntBuffer comp = MemoryUtil.memAllocInt(1);
		
		// load data from path.
		// width, height info is stored in length 1 buffer.
		image = STBImage.stbi_load(path, w, h, comp, 4);
		
		// failed to create image -> error
		if(image == null){
			throw new RuntimeException("Failed to load a texture file!"
                    + System.lineSeparator() + STBImage.stbi_failure_reason());
		}
		
		// create texture
		this.id = GL11.glGenTextures();
		
		// set size
		this.width = w.get(0);
		this.height = h.get(0);
		
		// bind texture
		this.bind();
		
		// set parameter
		this.setParameter(GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		this.setParameter(GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		this.setParameter(GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		this.setParameter(GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		// put image data.
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0,GL11.GL_RGBA8, width,
				height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
		
		// unbind + free data.
		this.unbind();
		STBImage.stbi_image_free(image);

	}
	
	public Texture(int width, int height, ByteBuffer data){
		this.width = width;
		this.height = height;
		
		// create texture
		this.id = GL11.glGenTextures();
		
		// bind texture
		this.bind();
		
		this.setParameter(GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		this.setParameter(GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		this.setParameter(GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		this.setParameter(GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		// put image data.
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0,GL11.GL_RGBA8, width,
				height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
				
		// unbind + free data.
		this.unbind();
		
	}

}
