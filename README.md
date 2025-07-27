# 🧬 NEOGENE: DNA Pattern Analysis Tool

NEOGENE is a JavaFX-based genomic pattern analyzer that visually detects specific DNA sequences within large genetic datasets using the Boyer-Moore algorithm.

## 🚀 Features

- Load DNA sequences from `.txt` files.
- Enter search patterns (e.g., `ATCG`).
- Real-time visualization of pattern matching.
- Custom DNA helix animation.
- Pattern alignment display with match highlighting.
- Execution time, comparisons, and match stats.
- Light/Dark mode toggle.
- Custom particle network background.

## 📂 Project Structure

```
Prj_NeoGene/
├── src/                  → Java source code (controllers, model, view)
├── styles/               → CSS styling for JavaFX
├── views/                → FXML layout files
├── javafx-sdk-xx/        → JavaFX SDK (local if not global)
├── README.md             → ← You are here
```

## 🧑‍💻 Requirements

- JDK 17+
- JavaFX SDK (download: https://gluonhq.com/products/javafx/)
- IntelliJ IDEA (recommended)

## ⚙️ Setup in IntelliJ IDEA

1. Download and extract JavaFX SDK.
2. Open this project in IntelliJ.
3. Go to `Run > Edit Configurations...`.
4. Set `Main class` to `application.App`.
5. In VM options, paste:

   ```
   --module-path C:\path\to\javafx-sdk-24.0.1\lib --add-modules javafx.controls,javafx.fxml
   ```

6. Apply and run with `Shift + F10`.

## 📎 Example Files

You can test the system with these example DNA sequence files:

- Genome_Aurora9.txt
- Specimen_ZetaPrime.txt
- Helix_TerraNova.txt

Each contains a valid DNA sequence with multiple potential matches.

## 📸 Screenshots

<img width="1913" height="986" alt="image" src="https://github.com/user-attachments/assets/6388d328-4daf-4ab6-ae1e-1522ad35096c" />

<img width="1905" height="983" alt="image" src="https://github.com/user-attachments/assets/805c9e04-6296-4c77-a630-83c438f38cda" />

<img width="1911" height="965" alt="image" src="https://github.com/user-attachments/assets/feac833e-0b03-4690-a5df-94a6bf13e8e2" />

<img width="1918" height="982" alt="image" src="https://github.com/user-attachments/assets/2acd748d-f2df-4ed4-a0d9-b3078c678f33" />



## 📚 Documentation

Key components are documented using Javadoc. Important classes:

- MainController.java
- BoyerMooreMatcher.java
- AdnHelixAnimator.java

## 📄 License

This project is for academic and educational purposes only.

## 👨‍🔬 Authors

- Santiago de la Cruz — @Santi-Dela-Cruz
- Escuela Politécnica Nacional — 2025A

