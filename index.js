
import { NativeModules, requireNativeComponent } from 'react-native';
import React, { Component, PureComponent, PropTypes } from 'react';
import {
  View,
  Button
} from 'react-native';


export class AlibcTradeWebView extends React.Component {
  constructor(props) {
    super(props);
    this._onTradeResult = this._onTradeResult.bind(this);
  }
  _onTradeResult(event) {
    if (!this.props.onChange) {
      return;
    }
    this.props.onChange(event.nativeEvent);
  }
  render() {
    return <NativeComponent {...this.props} onChange={this._onTradeResult} />;
  }
}

AlibcTradeWebView.propTypes = {
  param: PropTypes.object,
  onChange: React.PropTypes.func,
  ...View.propTypes,
};

const NativeComponent = requireNativeComponent("AlibcTradeWebView", AlibcTradeWebView, {nativeOnly: {onChange: true}});

const { RNAlibcSdk } = NativeModules;

export default RNAlibcSdk;
