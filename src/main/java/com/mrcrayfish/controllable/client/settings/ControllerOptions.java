package com.mrcrayfish.controllable.client.settings;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.mrcrayfish.controllable.Controllable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class ControllerOptions
{
    private static final DecimalFormat FORMAT = new DecimalFormat("0.0#");

    public static final SliderPercentageOption DEAD_ZONE = new ControllableSliderPercentageOption("controllable.options.deadZone", 0.0, 1.0, 0.01F, gameSettings -> {
        return Controllable.getOptions().deadZone;
    }, (gameSettings, value) -> {
        Controllable.getOptions().deadZone = MathHelper.clamp(value, 0.0, 1.0);
    }, (gameSettings, option) -> {
        double deadZone = Controllable.getOptions().deadZone;
        return I18n.format("controllable.options.deadZone.format", FORMAT.format(deadZone));
    });

    public static final SliderPercentageOption ROTATION_SPEED = new ControllableSliderPercentageOption("controllable.options.rotationSpeed", 1.0, 50.0, 1.0F, gameSettings -> {
        return Controllable.getOptions().rotationSpeed;
    }, (gameSettings, value) -> {
        Controllable.getOptions().rotationSpeed = MathHelper.clamp(value, 1.0, 50.0);
    }, (gameSettings, option) -> {
        double rotationSpeed = Controllable.getOptions().rotationSpeed;
        return I18n.format("controllable.options.rotationSpeed.format", FORMAT.format(rotationSpeed));
    });

    public static final SliderPercentageOption MOUSE_SPEED = new ControllableSliderPercentageOption("controllable.options.mouseSpeed", 1.0, 50.0, 1.0F, gameSettings -> {
        return Controllable.getOptions().mouseSpeed;
    }, (gameSettings, value) -> {
        Controllable.getOptions().mouseSpeed = MathHelper.clamp(value, 1.0, 50.0);
    }, (gameSettings, option) -> {
        double mouseSpeed = Controllable.getOptions().mouseSpeed;
        return I18n.format("controllable.options.mouseSpeed.format", FORMAT.format(mouseSpeed));
    });

    public static final Splitter COLON_SPLITTER = Splitter.on(':');

    private Minecraft minecraft;
    private File optionsFile;
    private double deadZone = 0.1;
    private double rotationSpeed = 20.0;
    private double mouseSpeed = 30.0;

    public ControllerOptions(Minecraft minecraft, File dataDir)
    {
        this.minecraft = minecraft;
        this.optionsFile = new File(dataDir, "controllable-options.txt");
        this.loadOptions();
    }

    private void loadOptions()
    {
        try
        {
            if(!this.optionsFile.exists())
            {
                return;
            }

            List<String> lines = IOUtils.readLines(new FileInputStream(this.optionsFile), Charsets.UTF_8);
            CompoundNBT compound = new CompoundNBT();

            for(String line : lines)
            {
                try
                {
                    Iterator<String> iterator = COLON_SPLITTER.omitEmptyStrings().limit(2).split(line).iterator();
                    compound.putString(iterator.next(), iterator.next());
                }
                catch(Exception var10)
                {
                    Controllable.LOGGER.warn("Skipping bad option: {}", line);
                }
            }

            for(String key : compound.keySet())
            {
                String value = compound.getString(key);

                try
                {
                    if("deadZone".equals(key))
                    {
                        DEAD_ZONE.set(minecraft.gameSettings, Double.parseDouble(value));
                    }
                    if("rotationSpeed".equals(key))
                    {
                        ROTATION_SPEED.set(minecraft.gameSettings, Double.parseDouble(value));
                    }
                    if("mouseSpeed".equals(key))
                    {
                        MOUSE_SPEED.set(minecraft.gameSettings, Double.parseDouble(value));
                    }
                }
                catch(Exception e)
                {
                    Controllable.LOGGER.warn("Skipping bad option: {}:{}", key, value);
                }
            }
        }
        catch(Exception e)
        {
            Controllable.LOGGER.error("Failed to load options", e);
        }

    }

    public void saveOptions()
    {
        try(PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8)))
        {
            writer.println("deadZone:" + FORMAT.format(this.deadZone));
            writer.println("rotationSpeed:" + FORMAT.format(this.rotationSpeed));
            writer.println("mouseSpeed:" + FORMAT.format(this.mouseSpeed));
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public double getDeadZone()
    {
        return deadZone;
    }

    public double getRotationSpeed()
    {
        return rotationSpeed;
    }

    public double getMouseSpeed()
    {
        return mouseSpeed;
    }
}
