using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Audio;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Bday
{
    public class Gift : Sprite
    {
        /// <summary>
        /// Initial ball speed. Constant
        /// </summary>
        public const float InitialSpeed = 0.1f;

        /// <summary>
        /// Defines a factor of speed increase when bumping on the paddle.
        /// Constant
        /// </summary>
        private static float SpeedIncreaseFactor = 1.015f;

        /// <summary>
        /// Defines ball size. Constant
        /// </summary>
        public const int GiftSize = 70;

        /// <summary>
        /// Defines current ball speed in time.
        /// </summary>
        public static float Speed { get; set; } = InitialSpeed;

        /// <summary>
        /// Sound gift makes when collected.
        /// </summary>
        public SoundEffect hitSound;

        /// <summary>
        /// Points player gets when this gifts is collected.
        /// Can be negative.
        /// </summary>
        public int pointsCollected;

        /// <summary>
        /// Number of lives player gets or looses when this gift is collected.
        /// </summary>
        public int livesAffected;

        /// <summary>
        /// Defines ball direction.
        /// Valid values (-1,-1), (1,1), (1,-1), (-1,1).
        /// Using Vector2 to simplify game calculation. Potentially
        /// dangerous because vector 2 can swallow other values as well.
        /// Think about creating your own, more suitable type.
        /// </summary>
        public Vector2 Direction;

        public Gift(Texture2D spriteTexture, bool ZigZag, SoundEffect sound, int pointsCollected, int livesAffected)
            : base(spriteTexture, GiftSize, GiftSize)
        {

            hitSound = sound;
            this.pointsCollected = pointsCollected;
            this.livesAffected = livesAffected;
            // Initial direction
            Direction = new Vector2(0, 1);
            if (ZigZag) {
                System.Random random = new System.Random();
                double direction = random.Next(-1, 3) - 0.5;
                Direction = new Vector2((float)direction, 1);
            }
            
        }

        public override void Draw(SpriteBatch spriteBatch, Color Color)
        {
            spriteBatch.Draw(Texture, Position, Size, Color);
        }

        public static void accelerate()
        {
            Speed *= SpeedIncreaseFactor;
        }


    }
}
