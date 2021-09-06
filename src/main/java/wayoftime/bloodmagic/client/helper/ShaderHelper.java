package wayoftime.bloodmagic.client.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

/**
 * This class was created by <Vazkii>. It's distributed as part of the Botania
 * Mod. Get the Source Code in github: https://github.com/Vazkii/Botania
 * <p>
 * Botania is Open Source and distributed under the Botania License:
 * http://botaniamod.net/license.php
 * <p>
 * File Created @ [Apr 9, 2014, 11:20:26 PM (GMT)]
 */
public final class ShaderHelper
{
	private static final int VERT_ST = ARBVertexShader.GL_VERTEX_SHADER_ARB;
	private static final int FRAG_ST = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;

	private static final int VERT = 1;
	private static final int FRAG = 2;

	private static final String VERT_EXTENSION = ".vsh";
	private static final String FRAG_EXTENSION = ".frag";

	public static int psiBar;

	public static void init()
	{
		if (!useShaders())
			return;

		psiBar = createProgram("/assets/bloodmagic/shaders/beam", FRAG);
	}

	public static void useShader(int shader, int ticks)
	{
		if (!useShaders())
			return;

		ARBShaderObjects.glUseProgramObjectARB(shader);

		if (shader != 0)
		{
			int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			ARBShaderObjects.glUniform1iARB(time, ticks);
		}
	}

	public static void releaseShader()
	{
		useShader(0, 0);
	}

	public static boolean useShaders()
	{
//        return OpenGlHelper.shadersSupported;
		return true;
	}

	private static int createProgram(String s, int sides)
	{
		boolean vert = (sides & VERT) != 0;
		boolean frag = (sides & FRAG) != 0;

		return createProgram(vert ? (s + VERT_EXTENSION) : null, frag ? (s + FRAG_EXTENSION) : null);
	}

	// Most of the code taken from the LWJGL wiki
	// http://lwjgl.org/wiki/index.php?title=GLSL_Shaders_with_LWJGL

	private static int createProgram(String vert, String frag)
	{
		int vertId = 0, fragId = 0, program = 0;
		if (vert != null)
			vertId = createShader(vert, VERT_ST);
		if (frag != null)
			fragId = createShader(frag, FRAG_ST);

		program = ARBShaderObjects.glCreateProgramObjectARB();
		if (program == 0)
			return 0;

		if (vert != null)
			ARBShaderObjects.glAttachObjectARB(program, vertId);
		if (frag != null)
			ARBShaderObjects.glAttachObjectARB(program, fragId);

		ARBShaderObjects.glLinkProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE)
		{
//            FMLLog.log(Level.ERROR, getLogInfo(program));
			return 0;
		}

		ARBShaderObjects.glValidateProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
		{
//            FMLLog.log(Level.ERROR, getLogInfo(program));
			return 0;
		}

		return program;
	}

	private static int createShader(String filename, int shaderType)
	{
		int shader = 0;
		try
		{
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if (shader == 0)
				return 0;

			ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
			ARBShaderObjects.glCompileShaderARB(shader);

			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

			return shader;
		} catch (Exception e)
		{
			ARBShaderObjects.glDeleteObjectARB(shader);
			e.printStackTrace();
			return -1;
		}
	}

	private static String getLogInfo(int obj)
	{
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	private static String readFileAsString(String filename)
			throws Exception
	{
		StringBuilder source = new StringBuilder();
		InputStream in = ShaderHelper.class.getResourceAsStream(filename);
		Exception exception = null;
		BufferedReader reader;

		if (in == null)
			return "";

		try
		{
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

			Exception innerExc = null;
			try
			{
				String line;
				while ((line = reader.readLine()) != null) source.append(line).append('\n');
			} catch (Exception exc)
			{
				exception = exc;
			} finally
			{
				try
				{
					reader.close();
				} catch (Exception exc)
				{
					if (innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}

			if (innerExc != null)
				throw innerExc;
		} catch (Exception exc)
		{
			exception = exc;
		} finally
		{
			try
			{
				in.close();
			} catch (Exception exc)
			{
				if (exception == null)
					exception = exc;
				else
					exc.printStackTrace();
			}

			if (exception != null)
				throw exception;
		}

		return source.toString();
	}

}
