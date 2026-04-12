# Multilingual Support Guide for `MultiLanguageAppSample`

This project already supports multiple languages using Android string resources.

## What is already in this project

### 1) English strings
File:
`app/src/main/res/values/strings.xml`

This file contains the default app language, which is English.

### 2) Hindi strings
File:
`app/src/main/res/values-hi/strings.xml`

This file contains the Hindi translation.

### 3) Compose screen uses the string resource
File:
`app/src/main/java/com/nutrino/multilanguageappsample/MainActivity.kt`

The UI uses:

```kotlin
Text(text = stringResource(R.string.sample_text))
```

That is the correct way to show translated text in Jetpack Compose.

### 4) RTL support is enabled
File:
`app/src/main/AndroidManifest.xml`

The app has:

```xml
android:supportsRtl="true"
```

This helps Android handle right-to-left layouts when needed.

---

# How Android multilingual support works

Android picks the right string automatically based on the device language.

Example:

- If the phone language is **English**, Android reads `values/strings.xml`
- If the phone language is **Hindi**, Android reads `values-hi/strings.xml`

You do **not** need to write `if` statements for language selection.

---

# Important rule

## Do not write text directly inside UI code

Bad example:

```kotlin
Text(text = "Hello")
```

Good example:

```kotlin
Text(text = stringResource(R.string.sample_text))
```

Why?

Because direct text is fixed in one language. Resource-based text can change automatically.

---

# Current project setup explained

Your project currently has:

## English
`app/src/main/res/values/strings.xml`

```xml
<string name="sample_text">Hello This is sample text transalted in multiple languages</string>
```

## Hindi
`app/src/main/res/values-hi/strings.xml`

```xml
<string name="sample_text">नमस्ते, यह कई भाषाओं में अनुवादित नमूना पाठ है।</string>
```

## Compose usage
`MainActivity.kt`

```kotlin
Text(text = stringResource(R.string.sample_text))
```

So when the user changes the phone language, the app will show the matching translation.

---

# How to add a new translated string

Suppose you want to add a new message called `welcome_text`.

## Step 1: Add it to the English file
Open:
`app/src/main/res/values/strings.xml`

Add:

```xml
<string name="welcome_text">Welcome to the app</string>
```

## Step 2: Add it to the Hindi file
Open:
`app/src/main/res/values-hi/strings.xml`

Add:

```xml
<string name="welcome_text">ऐप में आपका स्वागत है</string>
```

## Step 3: Use it in Compose
In Kotlin code:

```kotlin
Text(text = stringResource(R.string.welcome_text))
```

---

# How to add another language in Android Studio

Below is a very practical mouse-by-mouse guide.

## Option A: Create the language file manually

### Step 1: Open the Project panel
1. Open **Android Studio**.
2. On the left side, click **Project** view.
3. Expand:
   - `app`
   - `src`
   - `main`
   - `res`

### Step 2: Create a new values folder for the language
1. Right-click the `res` folder.
2. Click **New**.
3. Click **Android Resource Directory**.
4. In **Resource type**, select **values**.
5. In **Directory name**, Android Studio will suggest a locale folder.
6. Choose the language you want, such as:
   - `values-fr` for French
   - `values-es` for Spanish
   - `values-de` for German
7. Click **OK**.

### Step 3: Create strings.xml inside that folder
If Android Studio did not create `strings.xml` automatically:
1. Right-click the new folder.
2. Click **New**.
3. Click **Values Resource File**.
4. Name it `strings.xml`.
5. Click **OK**.

### Step 4: Copy all string keys
Open the default English file:
`app/src/main/res/values/strings.xml`

Copy every `<string>` key name.

Example:

```xml
<string name="app_name">Multi Language App Sample</string>
<string name="sample_text">Hello This is sample text transalted in multiple languages</string>
```

Then paste the same keys into the new language file and translate the values.

Important:
- Keep the **same key names**
- Only change the **text value**

---

# How to add Hindi correctly in Android Studio

You already have Hindi support, but here is the exact process to follow.

## Step 1: Open `strings.xml`
1. In Android Studio, open:
   `app/src/main/res/values/strings.xml`
2. Copy the string keys you want.

## Step 2: Open the Hindi file
1. Open:
   `app/src/main/res/values-hi/strings.xml`
2. Make sure the same keys exist there.

## Step 3: Add missing keys
If a key exists in English but not in Hindi, add it.

Example:

English:
```xml
<string name="login_button">Login</string>
```

Hindi:
```xml
<string name="login_button">लॉगिन</string>
```

---

# How to check translation in the app

## Method 1: Change the phone language
1. Open the device **Settings**.
2. Go to **System** or **General Management**.
3. Tap **Languages** or **Language and input**.
4. Set the language to **Hindi**.
5. Open your app again.

The app should display Hindi text automatically.

## Method 2: Use the emulator language
1. Open the Android Emulator.
2. Go to **Settings**.
3. Search for **Language**.
4. Change the system language to Hindi.
5. Relaunch the app.

---

# How to verify the text is coming from resources

Check your Compose code.

Correct:

```kotlin
Text(text = stringResource(R.string.sample_text))
```

Not correct:

```kotlin
Text(text = R.string.sample_text.toString())
```

The second version shows the numeric resource ID, not the translated text.

---

# Why `app_name` may have a missing translation warning

In `strings.xml`, you may see:

```xml
<string name="app_name" tools:ignore="MissingTranslation">Multi Language App Sample</string>
```

This means Android Studio is being told not to warn about missing translations for that one string.

If you want full multilingual support, add `app_name` to the Hindi file too:

```xml
<string name="app_name">मल्टी लैंग्वेज ऐप सैंपल</string>
```

If you do that, the warning can be removed later.

---

# Best practices for multilingual apps

## 1) Always use string resources
Use `stringResource(...)` in Compose.

## 2) Keep keys stable
Do not rename keys often. For example:
- good: `sample_text`
- avoid changing it to a new name every time

## 3) Translate every language file
If a key exists in English, it should also exist in Hindi and other languages.

## 4) Test every language manually
Change the emulator/device language and verify the screen.

## 5) Avoid hardcoded UI text
Hardcoded text cannot be translated automatically.

---

# Very short workflow summary

1. Write text in `values/strings.xml`
2. Add the same key in `values-hi/strings.xml`
3. Use `stringResource(R.string.your_key)` in Compose
4. Change device language to test

---

# Example from this project

## English file
`app/src/main/res/values/strings.xml`

```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <string name="app_name" tools:ignore="MissingTranslation">Multi Language App Sample</string>
    <string name="sample_text">Hello This is sample text transalted in multiple languages</string>
</resources>
```

## Hindi file
`app/src/main/res/values-hi/strings.xml`

```xml
<resources>
    <string name="sample_text">नमस्ते, यह कई भाषाओं में अनुवादित नमूना पाठ है।</string>
</resources>
```

## Compose usage
`MainActivity.kt`

```kotlin
@Composable
fun SampleApp() {
    Text(text = stringResource(R.string.sample_text))
}
```

That is the correct multilingual pattern.

---

# Final note

Your project is already set up the right way for multilingual support. The main job is:

- keep strings in resource files
- translate them in each language folder
- read them with `stringResource(...)`

If you want, I can also create a second markdown file with a **copy-paste checklist** for adding a new language like French or Spanish.
