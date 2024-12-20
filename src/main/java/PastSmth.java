import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.ui.components.*;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class PastSmth extends AnAction {

    private static String getCodeFromFile(String filename) {
        StringBuilder toPast = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(
                                PastSmth.class.getResourceAsStream(filename)
                        )
                )
        )
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                toPast.append(line);
                toPast.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return toPast.toString();
    }

    private static @NotNull Map<String, String> getOptions() throws FileNotFoundException {
        Map<String, String> options = new HashMap<>();

        options.put("BFS and DFS", getCodeFromFile("/BFS_and_DFS.txt"));
        options.put("Binary search tree native", getCodeFromFile("/Binary_Search_Tree.txt"));
        options.put("Binary search tree from preorder function", getCodeFromFile("/BST_from_preorder.txt"));
        options.put("Binary search tree with tree size and kth maximum", getCodeFromFile("/BST_with_size.txt"));
        options.put("Bitwise pref tree with search for y that x xor y is max", getCodeFromFile("/BPT_with_xor.txt"));
        options.put("Dijkstra algorithm", getCodeFromFile("/Dijkstra.txt"));
        options.put("Geometry functions", getCodeFromFile("/Geometry_Lib.txt"));
        options.put("LCA", getCodeFromFile("/LCA.txt"));
        options.put("One-ordered list", getCodeFromFile("/One_Ordered.txt"));
        options.put("Pref tree with search for k-th string", getCodeFromFile("/PrefTree_with_kth_str.txt"));
        options.put("Segment tree with addition on the segment", getCodeFromFile("/SegTree_with_add.txt"));
        options.put("Segment tree with scanline", getCodeFromFile("/SegTree_with_scanline.txt"));
        options.put("Two-ordered list", getCodeFromFile("/Two_Ordered.txt"));
        options.put("Z/pref-functions", getCodeFromFile("/Z_Pref.txt"));


        return options;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        Editor editor = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        Document doc = editor.getDocument();

        Map<String, String> options;
        try {
            options = getOptions();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        JBTextField searchField = new JBTextField();
        searchField.setPreferredSize(JBUI.size(200, 30));

        JBList<String> list = new JBList<>(options.keySet());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Map<String, String> finalOptions = options;
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String filter = searchField.getText().toLowerCase();
                List<String> filteredOptions = new ArrayList<>();
                for (String option : finalOptions.keySet()) {
                    if (option.toLowerCase().contains(filter)) {
                        filteredOptions.add(option);
                    }
                }
                Collections.sort(filteredOptions);
                list.setListData(filteredOptions.toArray(new String[0]));
            }
        });

        PopupChooserBuilder<String> popupBuilder = new PopupChooserBuilder<>(list);
        popupBuilder.setTitle("Choose Algorithm or Structure")
                .setMovable(true)
                .setResizable(true)
                .setNorthComponent(searchField).setItemChosenCallback(() -> {
                    String selectedOption = list.getSelectedValue();
                    if (selectedOption != null) {
                        String textToInsert = finalOptions.get(selectedOption);
                        if (textToInsert != null) {
                            WriteCommandAction.runWriteCommandAction(project, () -> doc.insertString(editor.getCaretModel().getOffset(), textToInsert));
                        }
                    }
                });

        popupBuilder.createPopup().showInFocusCenter();
    }
}
