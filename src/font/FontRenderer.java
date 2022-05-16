/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */


package src.font;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;



public class FontRenderer {
	public static final String VERTEX_SHADER = "src/TextShaders/fontVertexShader.txt";
	public static final String FRAGMENT_SHADER = "src/TextShaders/fontFragmentShader.txt";
	
	private fontShader shader;
	
	// ���� font renderer������ ���� vao, vbo.
	private int VAO;
	private int VBO;
	
	public FontRenderer(){
		shader = new fontShader(VERTEX_SHADER, FRAGMENT_SHADER);
		
		// projection matrix ���.
		shader.start();
		shader.setMatrix4f("projection", Maths.getProjectionMatrix(1000,1000));
		shader.stop();
		
		// create vao.
		VAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(VAO);
		
		VBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		
		// put empty data
		float[] vertices = new float[6 * 4];
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_DYNAMIC_DRAW);
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(0, 4, GL11.GL_FLOAT, false, 0, 0);
		
		// unbind vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		// unbind vao
		GL30.glBindVertexArray(0);
	}
	
	public void renderString(FontTexture fontTexture, String text, float x, float y, Vector3f color){
		shader.start();
		

		shader.setVector3f("textColor", color);

		float drawX = x;
		float drawY = y;
		

		fontTexture.getTexture().bind();
		
		// VAO bind.
		GL30.glBindVertexArray(VAO);
		GL20.glEnableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		
		for(int i=0; i< text.length(); i++){
			char ch = text.charAt(i);
			if(ch == '\n'){
				drawY += fontTexture.getHeight();
				drawX = x;
				continue;
			}
			// carriage return
			if(ch == '\r'){
				continue;
			}
			
			CharInfo cInfo = fontTexture.charMap.get(ch);
			float w = cInfo.getWidth();
			float h = fontTexture.getHeight();
			

			float tsx = cInfo.getStartX() / (float)fontTexture.getWidth();
			float tfx = (cInfo.getStartX() + cInfo.getWidth()) / (float)fontTexture.getWidth();
			
			// new data.
			float[] vertices = {
					drawX,     drawY + h,   tsx, 1.0f ,
					drawX + w, drawY,       tfx, 0.0f ,
					drawX,     drawY,       tsx, 0.0f ,

					drawX,     drawY + h,   tsx, 1.0f ,
					drawX + w, drawY + h,   tfx, 1.0f ,
					drawX + w, drawY,       tfx, 0.0f 
		        };
			drawX += cInfo.getWidth();
			
			// send new data and draw.
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertices);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);
			
		}
		
		// unbind vao, vbo.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		
		shader.stop();
		
	}

}
