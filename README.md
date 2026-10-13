# InkRead — Android handwriting reader (starter)

InkRead is an Android starter project for turning handwritten English notes into editable text.

## Architecture: offline-first, cloud-assisted

1. **On-device OCR (offline):** Google ML Kit Latin Text Recognition runs on the phone and gives a quick first pass. It may struggle with cursive or very poor handwriting.
2. **Cloud AI (optional):** The user can send the selected photo to a private backend endpoint for contextual handwriting interpretation. Cloud processing is optional and requires internet.
3. **Human review:** The result remains editable. Users should verify uncertain words, especially names, numbers, anatomy/medical terms, and abbreviations.

The app does not silently replace the local result. It has separate **Read offline** and **Improve with cloud AI** actions.

## Important status

This is source code, **not a compiled APK**. It has not been built or tested in this environment. Build it in Android Studio, then test on a real phone.

## Requirements

- Android Studio with a recent Android SDK (API 35 recommended)
- JDK 17
- Python 3.10+ for the optional backend
- An OpenAI API key for cloud recognition

## Build the Android app

1. Unzip this project.
2. Open the `InkRead-Android-Starter` folder in Android Studio.
3. Let Gradle sync and download dependencies.
4. Select **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. The debug APK is usually written to `app/build/outputs/apk/debug/app-debug.apk`.

## Use offline OCR

- Launch the app.
- Tap **Choose notebook photo** and select a photo.
- Tap **Read offline**. ML Kit performs OCR on the device after its model is available.
- Edit the result in the text box and tap **Copy text**.

The first use may require downloading ML Kit's recognition model, so install/test with internet once before relying on offline use.

## Set up cloud AI

### 1. Start the backend on your computer

```bash
cd backend
python -m venv .venv
# Windows: .venv\Scripts\activate
# macOS/Linux:
source .venv/bin/activate
pip install -r requirements.txt
```

Set your API key as an environment variable (do not put it in the Android app):

```bash
# macOS/Linux
export OPENAI_API_KEY="your-api-key"
# Windows PowerShell
$env:OPENAI_API_KEY="your-api-key"
```

Run the development server:

```bash
uvicorn main:app --host 0.0.0.0 --port 8000
```

### 2. Connect your Android phone

- Phone and computer must be on the same trusted Wi-Fi network.
- Find the computer's local IP address, e.g. `192.168.1.20`.
- In InkRead, enter `http://192.168.1.20:8000/` in the backend URL field.
- Allow port 8000 through your computer firewall if needed.
- Tap **Improve with cloud AI**.

`http://10.0.2.2:8000/` is the Android Emulator's special address for a server running on the same computer.

**Development-only warning:** this starter permits HTTP for local testing. Do not expose the development server to the public internet. For real deployment, use HTTPS, authentication, rate limits, file-size limits, abuse protection, and a privacy policy.

## Privacy

Cloud AI sends the selected notebook image to your configured backend, which forwards it to the AI provider. Only send images you have permission to process; avoid including sensitive personal information. Do not hard-code API keys in the app.

## Current limitations / next steps

- Recognition quality depends on handwriting, image quality, language, and context.
- ML Kit OCR is a fallback, not a handwriting-specialist model; the cloud AI path may do better but can still misread or infer words.
- The app currently accepts one image at a time. Multi-page PDFs, automatic crop/deskew, side-by-side original and transcript, export to TXT/PDF, uncertainty highlighting, and model/provider selection are follow-up features.
- Review all output before copying it into study notes.
