package com.mrcrayfish.controllable.client;

import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerState;
import com.studiohartman.jamepad.ControllerUnpluggedException;

import javax.annotation.Nullable;

public class Controller
{
    private Mappings.Entry mapping = null;
    private ControllerIndex index;
    private ControllerState state;
    private boolean[] states;

    public Controller(ControllerIndex index)
    {
        this.index = index;
        this.states = new boolean[Buttons.LENGTH];
    }

    /**
     * Gets the number of this controller
     *
     * @return the number of this controller
     */
    public int getNumber()
    {
        return index.getIndex();
    }

    /**
     * Gets the {@link ControllerIndex} instance of this controller. This allows for more raw access
     * to the controller. It is also useful for making controller rumble.
     *
     * @return the {@link ControllerIndex} instance of this controller
     */
    public ControllerIndex getIndex()
    {
        return index;
    }

    /**
     * Gets the {@link ControllerState} instance of this controller. This provides the current state
     * of all the buttons. This is useful for bypassing {@link #states} as a state can be false
     * (because it was cancelled) but is actually pressed down.
     *
     * @return the {@link ControllerState} instance of this controller
     */
    public ControllerState getState()
    {
        return state;
    }

    /**
     * Updates the {@link ControllerState} of this controller
     *
     * @param state the new {@link ControllerState} for this controller
     */
    public void updateState(ControllerState state)
    {
        this.state = state;
    }

    /**
     * Gets the LWJGL controller instance
     *
     * @return the lwjgl controller instance of this controller
     */
    public String getName()
    {
        if(index.isConnected())
        {
            try
            {
                return index.getName();
            }
            catch(ControllerUnpluggedException e)
            {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * Gets the value of the left trigger
     *
     * @return the left trigger value
     */
    public float getLTriggerValue()
    {
        return state.leftTrigger > 0.05F ? state.leftTrigger : 0F;
    }

    /**
     * Gets the value of the right trigger
     *
     * @return the right trigger value
     */
    public float getRTriggerValue()
    {
        return state.rightTrigger > 0.05F ? state.rightTrigger : 0F;
    }

    /**
     * Gets the left thumb stick x value
     *
     * @return the left thumb stick x value
     */
    public float getLThumbStickXValue()
    {
        return Math.abs(state.leftStickX) > 0.05F ? state.leftStickX : 0F;
    }

    /**
     * Gets the left thumb stick y value
     *
     * @return the left thumb stick y value
     */
    public float getLThumbStickYValue()
    {
        return Math.abs(state.leftStickY) > 0.05F ? state.leftStickY : 0F;
    }

    /**
     * Gets the right thumb stick x value
     *
     * @return the right thumb stick x value
     */
    public float getRThumbStickXValue()
    {
        return Math.abs(state.rightStickX) > 0.05F ? state.rightStickX : 0F;
    }

    /**
     * Gets the right thumb stick y value
     *
     * @return the right thumb stick y value
     */
    public float getRThumbStickYValue()
    {
        return Math.abs(state.rightStickY) > 0.05F ? state.rightStickY : 0F;
    }

    /**
     * Sets the mapping for this controller
     *
     * @param mapping the mapping to assign
     */
    public void setMapping(Mappings.Entry mapping)
    {
        this.mapping = mapping;
    }

    /**
     * Gets the mapping of this controller
     *
     * @return the mapping of this controller or null if not present
     */
    @Nullable
    public Mappings.Entry getMapping()
    {
        return mapping;
    }
}
