package main.java.helper;

import javax.swing.JDesktopPane;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.IOException;
import java.beans.PropertyVetoException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonManager {
    static void load_frame(JsonNode jsonNode, JComponent frame) throws IOException {
        try {
            var rect = frame.getBounds();
            int x = jsonNode.get(frame.getName() + ".x").asInt();
            int y = jsonNode.get(frame.getName() + ".y").asInt();
            int width = jsonNode.get(frame.getName() + ".width").asInt();
            int height = jsonNode.get(frame.getName() + ".height").asInt();
            frame.setBounds(new Rectangle(x, y, width, height));
            frame.setPreferredSize(new Dimension(width, height));
            if (frame instanceof JInternalFrame) {
                boolean is_icon = jsonNode.get(frame.getName() + ".is_icon").asBoolean(false);
                try {
                    ((JInternalFrame) frame).setIcon(is_icon);
                } catch (PropertyVetoException err) {
                }
            }
        } catch (Exception err) {
            throw new IOException();
        }
    }

    public static void load_positions(JDesktopPane pane) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File("position_data.json"));
            load_frame(jsonNode, pane);
            for (var frame : pane.getAllFrames()) {
                load_frame(jsonNode, frame);
            }
        } catch (IOException err) {
           save_positions(pane);
        }
    }

    static void log_frame(ObjectNode jsonNode, JComponent frame) {
        var rect = frame.getBounds();
        jsonNode.put(frame.getName() + ".x", rect.x);
        jsonNode.put(frame.getName() + ".y", rect.y);
        jsonNode.put(frame.getName() + ".width", rect.width);
        jsonNode.put(frame.getName() + ".height", rect.height);
        if (frame instanceof JInternalFrame) {
            jsonNode.put(frame.getName() + ".is_icon", ((JInternalFrame) frame).isIcon());
        }
    }

    public static void save_positions(JDesktopPane pane) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonNode = objectMapper.createObjectNode();
            log_frame(jsonNode, pane);
            for (var frame : pane.getAllFrames()) {
                log_frame(jsonNode, frame);
            }
            objectMapper.writeValue(new File("position_data.json"), jsonNode);
        } catch (IOException err) {
        }
    }
}
