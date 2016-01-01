# nanao

## About
A Japanese text similarity calculator based on tf-idf

## Usage

```java
// Japanese text.
String text = "徒然なるままに、日暮らし硯（すずり）に 向かいて、心に映りゆく由無し事を";

//  title-text pairs, which you want to know how they are similar to a given text.
Map<String, String> titleTextMap = new HashMap<String, String>();

titleTextMap.put("枕草子", "春は、あけぼの。やうやう白くなりゆく山ぎは少し明りて紫だちたる雲の細の冒頭くたなびきたる。");
titleTextMap.put("方丈記","行く川のながれは絶えずして、しかも本の水にあらず。");

// sorted title-score pairs.
List<TitleScore> result = new SimilarityCalculator().calcSimilarity(text, titleTextMap);
```
