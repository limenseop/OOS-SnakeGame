/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */


package src.font;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Maths {
	private static Vector2f[] compass = {
			new Vector2f(0,1),
			new Vector2f(1,0),
			new Vector2f(0,-1),
			new Vector2f(-1,0)
	};

	
	public static Matrix4f getProjectionMatrix(float width, float height) {
		Matrix4f mat = new Matrix4f();
		mat.ortho2D(0, width, height, 0);
		return mat;
	}
}
