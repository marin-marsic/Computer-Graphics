using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Bday
{
    public class SR : Sprite
    {
        /// <summary>
        /// Initial paddle speed. Constant
        /// </summary>
        private const float InitialSpeed = 0.6f;
        /// <summary>
        /// Paddle height. Constant
        /// </summary>
        private const int BagHeight = 169;
        /// <summary>
        /// Paddle width. Constant
        /// </summary>
        private const int BagWidth = 300;

        /// <summary>
        /// Current paddle speed in time
        /// </summary>
        public float Speed { get; set; }

        public SR(Texture2D spriteTexture)
            : base(spriteTexture, BagWidth, BagHeight)
        {
            Speed = InitialSpeed;
        }
    }
}
