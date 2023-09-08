def TCN(X, h0, kernel_size, dilations, num_layers):
  """
  Args:
    X: Input sequence
    h0: Initial hidden state
    kernel_size: Kernel size
    dilations: List of dilation rates
    num_layers: Number of layers

  Returns:
    Model output
  """

  h = h0
  for i in range(num_layers):
    h = causal_conv1d(h, kernel_size, dilations[i])

  return h

def causal_conv1d(X, kernel_size, dilation, padding):
  """
  Args:
    X: Input sequence
    kernel_size: Kernel size
    dilation: Dilation rate
    padding: Padding type

  Returns:
    Convolution output
  """

  return nn.Conv1d(
      in_channels=X.shape[-1],
      out_channels=X.shape[-1],
      kernel_size=kernel_size,
      dilation=dilation,
      padding=padding)(X)