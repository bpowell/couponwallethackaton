package gosale

import (
	"encoding/json"
	"os"
)

type Config struct {
	APIKEY   string
	Base_url string
	Service  string
	Partner  string
	Pos      string
}

func NewConfig() (*Config, error) {
	file, err := os.Open("key.js")
	if err != nil {
		return &Config{}, err
	}

	decoder := json.NewDecoder(file)
	var config Config
	err = decoder.Decode(&config)
	if err != nil {
		return &Config{}, err
	}

	return &config, nil
}
