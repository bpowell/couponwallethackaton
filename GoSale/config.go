package gosale

import (
	"encoding/json"
	"os"
)

type Config struct {
	APIKEY   string
	Base_Url string
	Service  string
	Partner  string
	Pos      string
}

func NewConfig() *Config {
	file, err := os.Open("key.js")
	if err != nil {
		return &Config{}
	}

	decoder := json.NewDecoder(file)
	var config Config
	err = decoder.Decode(&config)
	if err != nil {
		return &Config{}
	}

	return config
}
