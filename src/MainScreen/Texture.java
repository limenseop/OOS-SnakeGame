package MainScreen;

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
	
	public Texture(String path){
		IntBuffer w = MemoryUtil.memAllocInt(1);
		IntBuffer h = MemoryUtil.memAllocInt(1);
		ByteBuffer image;
		IntBuffer comp = MemoryUtil.memAllocInt(1);
		
		STBImage.stbi_set_flip_vertically_on_load(true);
		
		image = STBImage.stbi_load("Image/" + path, w, h, comp, 4);
		
		if(image == null){
			throw new RuntimeException("Failed to load a texture file!"
                    + System.lineSeparator() + STBImage.stbi_failure_reason());
		}
		
		this.id = GL11.glGenTextures();
		
		this.width = w.get(0);
		this.height = h.get(0);
		
		this.bind();
		
		this.setParameter(GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		this.setParameter(GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		this.setParameter(GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		this.setParameter(GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0,GL11.GL_RGBA8, width,
				height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
		
		this.unbind();
		STBImage.stbi_image_free(image);

	}

}




