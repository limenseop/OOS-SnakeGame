package src.font;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class fontShader {
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	// shader program �󿡼� position �� color�� �ش��ϴ� attrib index
	public int positionAttribIndex;
	public int colorAttribIndex;
	
	public int getProgramID() {
		return programID;
	}

	public fontShader(String vertexShaderFile, String fragmentShaderFile) {
		// 1. create two type of shader from txt file.
		vertexShaderID = loadShaderFromFile(vertexShaderFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShaderFromFile(fragmentShaderFile, GL20.GL_FRAGMENT_SHADER);

		// 2. create program and attach shader to program.
		programID = GL20.glCreateProgram();

		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		
		// bind vertex attribute to specific index.
		// GL20.glBindAttribLocation(programID, 0, "vertexData");
		// GL20.glBindAttribLocation(programID, 1, "colorData");

		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);

		// 3. delete shader -> no more need.
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);

	}

	public static int loadShaderFromFile(String fileName, int type) {
		StringBuilder shaderSource = new StringBuilder();

		// read each line and store source in string.
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}

		// create particular type of shader.
		int shaderID = GL20.glCreateShader(type);

		// attach source
		GL20.glShaderSource(shaderID, shaderSource);

		// check compile
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader.");
			System.exit(-1);
		}

		return shaderID;
	}

	public void start() {
		GL20.glUseProgram(programID);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}

	// register uniform varible in shader code.
	public void setFloat(String name, float value) {
		int loc = GL20.glGetUniformLocation(programID, name);
		GL20.glUniform1f(loc, value);
	}
	
	public void setInteger(String name, int value) {
		int loc = GL20.glGetUniformLocation(programID, name);
		GL20.glUniform1i(loc, value);
	}
	
	public void setInteger(String name, boolean value) {
		int loc = GL20.glGetUniformLocation(programID, name);
		GL20.glUniform1i(loc, value ? 1 : 0);
	}
	
	public void setVector2f(String name, float x, float y) {
		int loc = GL20.glGetUniformLocation(programID, name);
		GL20.glUniform2f(loc, x, y);
	}
	
	public void setVector2f(String name, Vector2f vec) {
		int loc = GL20.glGetUniformLocation(programID, name);
		GL20.glUniform2f(loc, vec.x, vec.y);
	}
	
	public void setVector3f(String name, Vector3f vec) {
		int loc = GL20.glGetUniformLocation(programID, name);
		GL20.glUniform3f(loc, vec.x, vec.y, vec.z);
	}
	
	public void setVector4f(String name, Vector4f vec) {
		int loc = GL20.glGetUniformLocation(programID, name);
		GL20.glUniform4f(loc, vec.x, vec.y, vec.z, vec.w);
	}
	
	// Matrix�� �Ѱ��ֱ� ���� �迭.
	private float[] matrix4fArr = new float[16];
	
	public void setMatrix4f(String name, Matrix4f matrix) {
		int loc = GL20.glGetUniformLocation(programID, name);
	
		matrix.get(matrix4fArr);
		GL20.glUniformMatrix4fv(loc, false, matrix4fArr);
	
	}

}
