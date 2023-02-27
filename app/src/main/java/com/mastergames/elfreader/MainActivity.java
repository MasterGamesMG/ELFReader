package com.mastergames.elfreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private EditText directoryOfFile;
    private Button readFile;
    private TextView archiveIsElf;
    private TextView architectureElf;
    private TextView elfEndian;
    private TextView elfABI;
    private TextView elfVersion;
    private TextView typeIsElfVersionABI;
    private TextView typeIsElf;
    private TextView typeIsElfPad;
    private Button clearOutputs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        directoryOfFile = findViewById(R.id.directoryoffile);
        readFile = findViewById(R.id.button_readfile);
        archiveIsElf = findViewById(R.id.isELForNot);
        architectureElf = findViewById(R.id.archictureiself);
        elfEndian = findViewById(R.id.elfEndian);
        elfABI = findViewById(R.id.elfAbi);
        elfVersion = findViewById(R.id.elfVersion);
        typeIsElf = findViewById(R.id.typeiself);
        clearOutputs = findViewById(R.id.clearoutput);

        readFile.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                try {
                    // Open the .so file and create a FileInputStream
                    String fileDirectory = directoryOfFile.getText().toString();
                    File file = new File(fileDirectory);
                    FileInputStream fis = new FileInputStream(file);

                    // Reading the first 52 bytes to get the ELF header
                    byte[] header = new byte[52];
                    fis.read(header);

                    // Get the ELF identifier
                    String magic = new String(header, 0, 4);
                    if (!magic.equals("\u007fELF")) {
                        archiveIsElf.setText("EI_MAG : " + "Unknown");
                    } else {
                        archiveIsElf.setText("EI_MAG : " + "0x7fELF (ELF)");
                    }

                    // Get elf class (32-bit or 64-bit)
                    int elfClass = header[4];
                    if (elfClass == 1) {
                        architectureElf.setText("EI_CLASS : " + "ELFCLASS32 (32-bit)");
                    } else if (elfClass == 2) {
                        architectureElf.setText("EI_CLASS : " + "ELFCLASS64 (64-bit)");
                    } else {
                        architectureElf.setText("EI_CLASS : " + "ELFCLASSNONE (This class is invalid)");
                    }

                    // Get data encoding
                    int dataEncoding = header[5];
                    if (dataEncoding == 1) {
                        elfEndian.setText("EI_DATA : " + "ELFDATA2LSB (Little-endian)");
                    } else if (dataEncoding == 2) {
                        elfEndian.setText("EI_DATA : " + "ELFDATA2MSB (Big-endian)");
                    } else {
                        elfEndian.setText("EI_DATA : " + "ELFDATANONE (Unknown data format)");
                    }

                    // Get the ELF version
                    int version = header[6];
                    if (version == 1) {
                        elfVersion.setText("EI_VERSION : " + "EV_CURRENT (Current version)");
                    } else {
                        elfVersion.setText("EI_VERSION : " + "EV_NONE (Invalid version)");
                    }

                    // Get the value of the EI_OSABI
                    byte osAbi = header[7];
                    String osAbiStr;
                    switch (osAbi) {
                        case 0:
                            osAbiStr = "ELFOSABI_NONE/ELFOSABI_SYSV (UNIX System V ABI)";
                            break;
                        case 1:
                            osAbiStr = "ELFOSABI_HPUX (HP-UX ABI)";
                            break;
                        case 2:
                            osAbiStr = "ELFOSABI_NETBSD (NetBSD ABI)";
                            break;
                        case 3:
                            osAbiStr = "ELFOSABI_LINUX (Linux ABI)";
                            break;
                        case 6:
                            osAbiStr = "ELFOSABI_SOLARIS (Solaris ABI)";
                            break;
                        case 8:
                            osAbiStr = "ELFOSABI_IRIX (IRIX ABI)";
                            break;
                        case 9:
                            osAbiStr = "ELFOSABI_FREEBSD (FreeBSD ABI)";
                            break;
                        case 10:
                            osAbiStr = "ELFOSABI_TRU64 (TRU64 UNIX ABI)";
                            break;
                        case 97:
                            osAbiStr = "ELFOSABI_ARM (ARM architecture ABI)";
                            break;
                        case (byte) 255:
                            osAbiStr = "ELFOSABI_STANDALONE (Stand-alone (embedded) ABI)";
                            break;
                        default:
                            osAbiStr = "Unknown";
                            break;
                    }
                    elfABI.setText("EI_OSABI : " + osAbiStr);

                    // Get ELF file type
                    int type = header[16] + (header[17] << 8);
                    String typeStr = "";
                    switch (type) {
                        case 1:
                            typeStr = "ET_REL (A Relocatable file)";
                            break;
                        case 2:
                            typeStr = "ET_EXEC (An Executable file)";
                            break;
                        case 3:
                            typeStr = "ET_DYN (A Shared object file)";
                            break;
                        case 4:
                            typeStr = "ET_CORE (A Core file)";
                            break;
                        default:
                            typeStr = "ET_NONE (An unknown type)";
                            break;
                    }
                    typeIsElf.setText("ET : " + typeStr);

                    // Close FileInputStream
                    fis.close();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "The file does not exist", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Failed to read the file", Toast.LENGTH_LONG).show();
                }
            }
        });

        clearOutputs.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                archiveIsElf.setText("EI_MAG : ");
                architectureElf.setText("EI_CLASS : ");
                elfEndian.setText("EI_DATA : ");
                elfVersion.setText("EI_VERSION : ");
                elfABI.setText("EI_OSABI : ");
                typeIsElf.setText("ET : ");
            }
        });
    }
}