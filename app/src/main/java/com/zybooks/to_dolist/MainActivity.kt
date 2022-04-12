package com.zybooks.to_dolist

import ToDoList
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), DeleteAllDialog.OnYesClickListener {
    private var toDoList = ToDoList(this)
    private lateinit var itemEditText: EditText
    private lateinit var listTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemEditText = findViewById(R.id.todo_item)
        listTextView = findViewById(R.id.item_list)
        findViewById<Button>(R.id.add_button).setOnClickListener { addButtonClick() }
        findViewById<Button>(R.id.settings_button).setOnClickListener { onClickSettings() }
        findViewById<Button>(R.id.clear_button).setOnClickListener { clearButtonClick() }
    }

    override fun onResume() {
        super.onResume()

        // Attempt to load a previously saved list
        toDoList.readFromFile()
        displayList()
    }

    override fun onPause() {
        super.onPause()

        // Save list for later
        toDoList.saveToFile()
    }

    private fun addButtonClick() {

        // Ignore any leading or trailing spaces
        val item = itemEditText.text.toString().trim()

        // Clear the EditText so it's ready for another item
        itemEditText.setText("")

        // Add the item to the list and display it
        if (item.isNotEmpty()) {
            toDoList.addItem(item)
            displayList()
        }
    }

    private fun displayList() {

        // Display a numbered list of items
        val itemText = StringBuffer()
        val items = toDoList.getItems()
        val lineSeparator = System.getProperty("line.separator")

        for (i in items.indices) {
            itemText.append(i + 1).append(". ").append(items[i]).append(lineSeparator)
        }

        listTextView.text = itemText.toString()
    }

    override fun onYesClick() {
        toDoList.clear()
        displayList()
    }

    private fun clearButtonClick() {
        val dialog = DeleteAllDialog()
        dialog.show(supportFragmentManager, "warningDialog")
    }

    private fun onClickSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
