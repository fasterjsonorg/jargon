# Jargon

Jargon is a fast JSON library for the JVM.

Jargon is geared towards applications that process large numbers of JSON
documents. It is particularly suitable for real-time applications, such as game
servers or trading systems, that aim to maintain consistently high throughput
or low latency.

Jargon requires Java Runtime Environment (JRE) 8 or newer.

## Status

Jargon is currently in early stages of development.

## Features

- Jargon does not allocate memory on the fast path. This is helpful when
  working with JSON documents of all sizes.

- The core objects in the Jargon API are reusable. This is helpful when working
  with a large number of JSON documents.

- The Jargon API is inspired by the Jackson API. This makes working with Jargon
  easier if you are familiar with Jackson.

## Limitations

- Jargon only supports the ASCII and UTF-8 character sets.

- Although the Jargon API resembles the Jackson API, Jargon implements only a
  fraction of Jackson's functionality.

## Roadmap

| Milestone |   Status    | Description                             |
|----------:|:-----------:|-----------------------------------------|
|         1 | In Progress | A `JsonParser` API with ASCII support   |
|         2 |   Planned   | A `JsonNode` API                        |
|         3 |   Planned   | UTF-8 support                           |
|         4 |   Planned   | Scientific notation support for numbers |
|         5 |   Planned   | Escape code support for strings         |

## Links

- Follow [@fasterjsonorg](https://twitter.com/fasterjsonorg) on Twitter for
  news and announcements

## License

Copyright 2021 Jargon authors.

Released under the Apache License, Version 2.0. See `LICENSE.txt` for details.
